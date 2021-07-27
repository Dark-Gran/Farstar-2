package com.darkgran.farstar.battle;

import com.darkgran.farstar.cards.*;
import com.darkgran.farstar.gui.battlegui.DropTarget;
import com.darkgran.farstar.gui.tokens.Token;
import com.darkgran.farstar.battle.players.*;

import java.util.ArrayList;
import java.util.ListIterator;

import static com.darkgran.farstar.cards.EffectTypeSpecifics.ChangeStatType.DEFENSE_TYPE;
import static com.darkgran.farstar.cards.EffectTypeSpecifics.ChangeStatType.OFFENSE_TYPE;

public class AbilityManager {
    private final Battle battle;

    public AbilityManager(Battle battle) {
        this.battle = battle;
    }

    public boolean playAbility(Token casterToken, BattleCard target, AbilityInfo ability, DropTarget dropTarget) { //in-future: use List for "target" to enable multi-targeting (atm no such battleCards)
        boolean success = false;
        BattleCard caster = casterToken.getCard();
        if (caster != null && caster.getCardInfo().getAbilities() != null) {
            if (ability != null && ability.getEffects() != null) {
                //TARGETING
                ArrayList<BattleCard> targets = new ArrayList<>();
                if (target == null) {
                    AbilityTargets abilityTargets = ability.getTargets();
                    switch (abilityTargets) {
                        case NONE:
                        case SELF:
                            targets.add(casterToken.getCard());
                            break;
                        case ANY:
                        case ANY_ALLY:
                        case ALLIED_FLEET:
                        case ALLIED_MS:
                        case ANY_ENEMY:
                        case ENEMY_MS:
                        case ENEMY_FLEET:
                            getBattle().getRoundManager().askForTargets(casterToken, ability, dropTarget);
                            break;
                        case ENTIRE_ENEMY_FLEET:
                            BattlePlayer[] enemies = getBattle().getEnemies(caster.getBattlePlayer());
                            for (BattlePlayer enemy : enemies) {
                                for (int i = 0; i < enemy.getFleet().getShips().length; i++) {
                                    if (enemy.getFleet().getShips()[i] != null) {
                                        targets.add(enemy.getFleet().getShips()[i]);
                                    }
                                }
                            }
                            break;
                        case ENTIRE_ALLIED_FLEET:
                            for (int i = 0; i < caster.getBattlePlayer().getFleet().getShips().length; i++) {
                                if (caster.getBattlePlayer().getFleet().getShips()[i] != null) {
                                    targets.add(caster.getBattlePlayer().getFleet().getShips()[i]);
                                }
                            }
                            break;
                    }
                } else if (validAbilityTarget(ability, caster, target)) {
                    targets.add(target);
                }
                //EXECUTION
                ArrayList<BattlePlayer> targetedPlayers = new ArrayList<>();
                for (BattleCard currentTarget : targets) {
                    if (currentTarget != null) {
                        for (int i = 0; i < ability.getEffects().size(); i++) {
                            if (ability.getEffects().get(i) != null) {
                                if (!success) {
                                    success = executeEffect(currentTarget, ability.getEffects().get(i), false, caster);
                                } else {
                                    executeEffect(currentTarget, ability.getEffects().get(i), false, caster);
                                }
                            }
                        }
                        if (success) {
                            currentTarget.addToHistory(caster, ability);
                            if (!targetedPlayers.contains(currentTarget.getBattlePlayer())) {
                                targetedPlayers.add(currentTarget.getBattlePlayer());
                            }
                        }
                    }
                }
                for (BattlePlayer targetPlayer : targetedPlayers) {
                    getBattle().getCombatManager().checkPlayerForAftermath(targetPlayer);
                }
            }
        }
        return success;
    }

    //-------------//
    //-EFFECT-LIST-//
    //-------------//

    public boolean executeEffect(BattleCard target, Effect effect, boolean reverse, BattleCard caster) {
        boolean success = false;
        if (effect.getEffectType() != null) {
            switch (effect.getEffectType()) {
                case NONE:
                    success = true;
                    break;
                case CHANGE_STAT:
                    if (!reverse) { success = changeStat(target, effect); }
                    else { success = reverseStat(target, effect); }
                    break;
                case DEAL_DMG:
                    if (!reverse) { success = dealDmg(target, effect, caster); }
                    break;
                case REPAIR:
                    if (!reverse) { success = repair(target, effect); }
                    break;
                case CHANGE_RESOURCE:
                    success = changeResource(target, effect, reverse);
                    break;
            }
        }
        return success;
    }

    //CHANGE_STAT
    private boolean changeStat(BattleCard target, Effect effect) {
        boolean success = false;
        boolean doNotSaveEffect = false;
        if (effect.getEffectInfo() != null && effect.getEffectInfo().size() >= 2 && effect.getEffectInfo().get(0) != null && effect.getEffectInfo().get(1) != null) {
            Object changeInfo = effect.getEffectInfo().get(1);
            EffectTypeSpecifics.ChangeStatType changeStatType = EffectTypeSpecifics.ChangeStatType.valueOf(effect.getEffectInfo().get(0).toString());
            TechType techType;
            switch (changeStatType) {
                case OFFENSE:
                    target.getCardInfo().changeOffense(floatObjectToInt(changeInfo));
                    target.refreshToken(false, true);
                    success = true;
                    break;
                case DEFENSE:
                    target.getCardInfo().changeDefense(floatObjectToInt(changeInfo));
                    target.refreshToken(true, false);
                    success = true;
                    break;
                case OFFENSE_TYPE:
                    techType = TechType.valueOf(changeInfo.toString());
                    target.getCardInfo().setOffenseType(techType);
                    target.refreshToken(false, true);
                    success = true;
                    break;
                case DEFENSE_TYPE:
                    techType = TechType.valueOf(changeInfo.toString());
                    target.getCardInfo().setDefenseType(techType);
                    target.refreshToken(true, false);
                    success = true;
                    break;
                case ABILITY:
                    if (effect.getEffectInfo().get(2) != null) {
                        AbilityInfo newAbility = effectToAbility(effect.getEffectInfo(), effect.getDuration());
                        for (int i = 0; i < target.getCardInfo().getAbilities().size() && !success; i++) {
                            if (isTheSameAbility(target.getCardInfo().getAbilities().get(i), newAbility)) {
                                success = true;
                                doNotSaveEffect = true;
                            }
                        }
                        if (!success) {
                            target.getCardInfo().addAbility(newAbility); //in-future: When adding Reach where it's already present, upgrade with it instead (ie. sum)
                            success = true;
                        }
                    }
                    break;
            }
            if (!doNotSaveEffect) {
                saveEffect(target, instanceEffect(effect));
            }
        }
        return success;
    }

    //belongs to CHANGE_STAT
    private boolean reverseStat(BattleCard target, Effect effect) {
        boolean success = false;
        if (effect.getEffectInfo() != null && effect.getEffectInfo().get(0) != null && effect.getEffectInfo().get(1) != null) {
            Object changeInfo = effect.getEffectInfo().get(1);
            EffectTypeSpecifics.ChangeStatType changeStatType = EffectTypeSpecifics.ChangeStatType.valueOf(effect.getEffectInfo().get(0).toString());
            switch (changeStatType) {
                case OFFENSE:
                    target.getCardInfo().changeOffense(-floatObjectToInt(changeInfo));
                    target.refreshToken(false, true);
                    success = true;
                    break;
                case DEFENSE:
                    target.getCardInfo().changeDefense(-floatObjectToInt(changeInfo));
                    target.refreshToken(true, false);
                    success = true;
                    break;
                case OFFENSE_TYPE:
                case DEFENSE_TYPE:
                    reverseType(target, effect, changeStatType);
                    target.refreshToken(changeStatType == DEFENSE_TYPE, changeStatType == OFFENSE_TYPE);
                    success = true;
                    break;
                case ABILITY:
                    for (int i = 0; i < target.getCardInfo().getAbilities().size() && !success; i++) {
                        if (isTheSameAbility(target.getCardInfo().getAbilities().get(i), effectToAbility(effect.getEffectInfo(), effect.getDuration()))) {
                            target.getCardInfo().getAbilities().remove(i);
                            success = true;
                        }
                    }
                    success = true;
                    break;
            }
        }
        return success;
    }

    //DEAL_DMG
    private boolean dealDmg(BattleCard target, Effect effect, BattleCard caster) {
        if (effect.getEffectInfo() != null && effect.getEffectInfo().size() >= 2 && effect.getEffectInfo().get(0) != null && effect.getEffectInfo().get(1) != null) {
            int power = floatObjectToInt(effect.getEffectInfo().get(0));
            TechType techType = TechType.valueOf(effect.getEffectInfo().get(1).toString());
            int dmg = DuelManager.getDmgAgainstShields(power, target.getHealth(), techType, target.getCardInfo().getDefenseType());
            if (caster != null) {
                getBattle().getBattleScreen().getBattleStage().getShotManager().newAttack(caster.getBattlePlayer().getMs().getToken(), target.getToken(), power, techType, caster.getCardInfo().getAnimatedShots());
            }
            if (target instanceof Ship) {
                target.receiveDMG(dmg);
            } else {
                if (!target.receiveDMG(dmg)) { target.death(); }
            }
            return true;
        }
        return false;
    }

    //REPAIR
    private boolean repair(BattleCard target, Effect effect) {
        if (effect.getEffectInfo() != null && effect.getEffectInfo().size() >= 1 && effect.getEffectInfo().get(0) != null) {
            int dmg = floatObjectToInt(effect.getEffectInfo().get(0));
            target.repairDMG(dmg);
            return true;
        }
        return false;
    }

    //CHANGE_RESOURCE
    private boolean changeResource(BattleCard target, Effect effect, boolean reverse) {
        if (effect.getEffectInfo() != null && effect.getEffectInfo().get(0) != null && effect.getEffectInfo().get(1) != null) {
            Object changeInfo = effect.getEffectInfo().get(1);
            EffectTypeSpecifics.ChangeResourceType resource = EffectTypeSpecifics.ChangeResourceType.valueOf(effect.getEffectInfo().get(0).toString());
            int change = floatObjectToInt(changeInfo);
            if (reverse) { change *= -1; }
            switch (resource) {
                case ENERGY:
                    target.getBattlePlayer().addEnergy(change);
                    return true;
                case MATTER:
                    target.getBattlePlayer().addMatter(change);
                    return true;
            }
        }
        return false;
    }

    //-----------//
    //-UTILITIES-//
    //-----------//

    public static boolean validAbilityTarget(AbilityInfo abilityInfo, BattleCard caster, BattleCard target) { //in-future: support for other modes than 1v1
        switch (abilityInfo.getTargets()) {
            default:
                return false;
            case NONE:
            case ANY:
                return true;
            case SELF:
                return caster==target;
            case ANY_ALLY:
            case ENTIRE_ALLIED_FLEET:
                return caster.getBattlePlayer()==target.getBattlePlayer();
            case ANY_ENEMY:
            case ENTIRE_ENEMY_FLEET:
                return caster.getBattlePlayer()!=target.getBattlePlayer();
            case ALLIED_FLEET:
                return caster.getBattlePlayer()==target.getBattlePlayer() && !(target instanceof Mothership);
            case ENEMY_FLEET:
                return caster.getBattlePlayer()!=target.getBattlePlayer() && !(target instanceof Mothership);
            case ALLIED_MS:
                return caster.getBattlePlayer()==target.getBattlePlayer() && target instanceof Mothership;
            case ENEMY_MS:
                return caster.getBattlePlayer()!=target.getBattlePlayer() && target instanceof Mothership;

        }
    }

    public static boolean hasStarter(BattleCard battleCard, AbilityStarter abilityStarter) {
        for (AbilityInfo abilityInfo : battleCard.getCardInfo().getAbilities()) {
            if (abilityInfo.getStarter() == abilityStarter) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasEffectType(BattleCard battleCard, EffectType effectType) {
        if (effectType != null) {
            for (AbilityInfo abilityInfo : battleCard.getCardInfo().getAbilities()) {
                for (Effect effect : abilityInfo.getEffects()) {
                    if (effect.getEffectType() == effectType) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean hasAttribute(BattleCard battleCard, EffectType effectType) { //checks for abilities with "starter=NONE" (old "attributes")
        if (effectType != null) {
            for (AbilityInfo abilityInfo : battleCard.getCardInfo().getAbilities()) {
                if (abilityInfo.getStarter() == AbilityStarter.NONE) {
                    for (Effect effect : abilityInfo.getEffects()) {
                        if (effect.getEffectType() == effectType) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static int getReach(BattleCard battleCard) {
        for (AbilityInfo abilityInfo : battleCard.getCardInfo().getAbilities()) {
            if (abilityInfo.getStarter() == AbilityStarter.NONE) {
                for (Effect effect : abilityInfo.getEffects()) {
                    if (effect.getEffectType() == EffectType.REACH) {
                        if (effect.getEffectInfo().size() > 0 && effect.getEffectInfo().get(0) != null) {
                            return floatObjectToInt(effect.getEffectInfo().get(0));
                        }
                    }
                }
            }
        }
        return 0;
    }

    public static boolean upgradesFirstStrike(BattleCard battleCard) {
        for (AbilityInfo abilityInfo : battleCard.getCardInfo().getAbilities()) {
            for (Effect effect : abilityInfo.getEffects()) {
                if (effect.getEffectType() == EffectType.CHANGE_STAT) {
                    if (effect.getEffectInfo() != null && effect.getEffectInfo().get(0) != null && effect.getEffectInfo().get(1) != null) {
                        if (effect.getEffectInfo().get(0).toString().equals("ABILITY") && effect.getEffectInfo().get(1).toString().equals("FIRST_STRIKE")) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static int floatObjectToInt(Object obj) {
        if (obj instanceof Float) {
            float f = (Float) obj;
            return (int) f;
        }
        return 0;
    }

    private static boolean isTheSameAbility(AbilityInfo abilityA, AbilityInfo abilityB) {
        if (abilityA != null && abilityB != null) {
            return (abilityA.getStarter() == abilityB.getStarter()) && isTheSameEffectsList(abilityA.getEffects(), abilityB.getEffects());
        }
        return (abilityA == abilityB);
    }

    private static boolean isTheSameEffectsList(ArrayList<Effect> effectsA, ArrayList<Effect> effectsB) {
        if (effectsA != null && effectsB != null) {
            if (effectsA.size() == effectsB.size()) {
                for (int i = 0; i < effectsA.size(); i++) {
                    if ((effectsA.get(i).getEffectType() != effectsB.get(i).getEffectType()) && (effectsA.get(i).getEffectInfo() != effectsB.get(i).getEffectInfo())) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }
        return (effectsA == effectsB);
    }

    private void saveEffect(BattleCard target, Effect effect) {
        if (getBattle().getCombatManager().isTacticalPhase()) {
            effect.setDuration(effect.getDuration()-1);
        }
        target.addToEffects(effect);
    }

    /** Only Strings and Floats are expected: See CardLibrary jsonReader for comment on this raw type */
    private AbilityInfo effectToAbility(ArrayList effectInfo, int effectDuration) { //creates new ability-attribute
        EffectType effectType = EffectType.valueOf(effectInfo.get(1).toString());
        Effect newEffect = new Effect();
        newEffect.setEffectType(effectType);
        ArrayList info = new ArrayList<>(effectInfo);
        if (info.size() >= 4) { info.set(0, effectInfo.get(4)); }
        newEffect.setEffectInfo(info);
        newEffect.setDuration(effectDuration);
        ArrayList<Effect> newAbilityEffects = new ArrayList<>();
        newAbilityEffects.add(newEffect);
        AbilityTargets abilityTargets = AbilityTargets.NONE;
        if (info.size() >= 5) { abilityTargets = AbilityTargets.valueOf(effectInfo.get(5).toString()); }
        return new AbilityInfo(AbilityStarter.valueOf(effectInfo.get(2).toString()), newAbilityEffects, new ResourcePrice(), abilityTargets);
    }

    private void reverseType(BattleCard target, Effect effect, EffectTypeSpecifics.ChangeStatType type) {
        boolean success = false;
        ListIterator<Effect> li = target.getEffects().listIterator(target.getEffects().size());
        Effect otherEffect;
        while (!success && li.hasPrevious()) {
            otherEffect = li.previous();
            if (otherEffect != effect && otherEffect.getEffectType() == EffectType.CHANGE_STAT) {
                if (EffectTypeSpecifics.ChangeStatType.valueOf(otherEffect.getEffectInfo().get(0).toString()) == type) {
                    Object changeInfo = otherEffect.getEffectInfo().get(1);
                    TechType techType = TechType.valueOf(changeInfo.toString());
                    if (type == OFFENSE_TYPE) {
                        target.getCardInfo().setOffenseType(techType);
                    } else if (type == DEFENSE_TYPE) {
                        target.getCardInfo().setDefenseType(techType);
                    }
                    success = true;
                }
            }
        }
        if (!success) {
            if (type == OFFENSE_TYPE) {
                target.getCardInfo().setOffenseType(target.getOriginalInfo().getOffenseType());
            } else if (type == DEFENSE_TYPE) {
                target.getCardInfo().setDefenseType(target.getOriginalInfo().getDefenseType());
            }
        }
    }

    private Effect instanceEffect(Effect effect) {
        return new Effect(effect.getEffectType(), effect.getEffectInfo(), effect.getDuration());
    }

    public Battle getBattle() { return battle; }

}
