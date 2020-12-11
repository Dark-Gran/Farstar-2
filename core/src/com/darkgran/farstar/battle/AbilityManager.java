package com.darkgran.farstar.battle;

import com.darkgran.farstar.battle.gui.DropTarget;
import com.darkgran.farstar.battle.gui.Token;
import com.darkgran.farstar.battle.players.*;
import com.darkgran.farstar.battle.players.abilities.*;

import java.util.ArrayList;
import java.util.ListIterator;

import static com.darkgran.farstar.battle.players.abilities.EffectTypeSpecifics.ChangeStatType.DEFENSE_TYPE;
import static com.darkgran.farstar.battle.players.abilities.EffectTypeSpecifics.ChangeStatType.OFFENSE_TYPE;

public class AbilityManager { //TODO: 1.death on dealDmg, 2.cancel abilityTargeting, 3. use only once per turn
    private final Battle battle;

    public AbilityManager(Battle battle) {
        this.battle = battle;
    }

    public boolean playAbility(Token casterToken, Card target, int abilityIx, DropTarget dropTarget) {
        boolean success = false;
        Card caster = casterToken.getCard();
        if (caster != null && caster.getCardInfo().getAbilities() != null) {
            AbilityInfo ability = caster.getCardInfo().getAbilities().get(abilityIx);
            if (ability != null && ability.getEffects() != null) {
                //TARGETING
                if (target == null) {
                    AbilityTargets abilityTargets = ability.getTargets();
                    switch (abilityTargets) {
                        case SELF:
                            target = casterToken.getCard();
                            break;
                        case ANY:
                            getBattle().getRoundManager().askForTargets(casterToken, abilityIx, dropTarget);
                            break;
                    }
                }
                //EXECUTION
                if (target != null) {
                    for (int i = 0; i < ability.getEffects().size(); i++) {
                        if (ability.getEffects().get(i) != null) {
                            if (!success) {
                                success = executeEffect(target, ability.getEffects().get(i), false);
                            } else {
                                executeEffect(target, ability.getEffects().get(i), false);
                            }
                        }
                    }
                    if (success) { target.addToHistory(caster, abilityIx); }
                }
            }
        }
        return success;
    }

    //-------------//
    //-EFFECT-LIST-//
    //-------------//

    public boolean executeEffect(Card target, Effect effect, boolean reverse) {
        boolean success = false;
        if (effect.getEffectType() != null) {
            switch (effect.getEffectType()) {
                case NONE:
                    success = true;
                    break;
                case CHANGE_STAT:
                    if (!reverse) { success = changeStat(target, effect, reverse); }
                    else { success = reverseStat(target, effect); }
                    break;
                case DEAL_DMG:
                    if (!reverse) { success = dealDmg(target, effect); }
                    break;
            }
        }
        return success;
    }

    //CHANGE_STAT
    private boolean changeStat(Card target, Effect effect, boolean reverse) {
        boolean success = false;
        boolean dontSaveEffect = false;
        if (effect.getEffectInfo() != null && effect.getEffectInfo().get(0) != null && effect.getEffectInfo().get(1) != null) {
            Object changeInfo = effect.getEffectInfo().get(1);
            EffectTypeSpecifics.ChangeStatType changeStatType = EffectTypeSpecifics.ChangeStatType.valueOf(effect.getEffectInfo().get(0).toString());
            TechType techType;
            switch (changeStatType) {
                case OFFENSE:
                    target.getCardInfo().changeOffense(floatObjectToInt(changeInfo));
                    success = true;
                    break;
                case DEFENSE:
                    target.getCardInfo().changeDefense(floatObjectToInt(changeInfo));
                    success = true;
                    break;
                case OFFENSE_TYPE:
                    techType = TechType.valueOf(changeInfo.toString());
                    target.getCardInfo().setOffenseType(techType);
                    success = true;
                    break;
                case DEFENSE_TYPE:
                    techType = TechType.valueOf(changeInfo.toString());
                    target.getCardInfo().setDefenseType(techType);
                    success = true;
                    break;
                case ABILITY:
                    if (effect.getEffectInfo().get(2) != null) {
                        AbilityInfo newAbility = effectToAbility(effect.getEffectInfo(), effect.getDuration());
                        for (int i = 0; i < target.getCardInfo().getAbilities().size() && !success; i++) {
                            if (isTheSameAbility(target.getCardInfo().getAbilities().get(i), newAbility)) {
                                success = true;
                                dontSaveEffect = true;
                            }
                        }
                        if (!success) {
                            target.getCardInfo().addAbility(newAbility);
                            success = true;
                        }
                    }
                    break;
            }
            if (!reverse && !dontSaveEffect) {
                saveEffect(target, InstanceFactory.instanceEffect(effect));
            }
        }
        return success;
    }

    //belongs to CHANGE_STAT
    private boolean reverseStat(Card target, Effect effect) {
        boolean success = false;
        if (effect.getEffectInfo() != null && effect.getEffectInfo().get(0) != null && effect.getEffectInfo().get(1) != null) {
            Object changeInfo = effect.getEffectInfo().get(1);
            EffectTypeSpecifics.ChangeStatType changeStatType = EffectTypeSpecifics.ChangeStatType.valueOf(effect.getEffectInfo().get(0).toString());
            switch (changeStatType) {
                case OFFENSE:
                    target.getCardInfo().changeOffense(-floatObjectToInt(changeInfo));
                    success = true;
                    break;
                case DEFENSE:
                    target.getCardInfo().changeDefense(-floatObjectToInt(changeInfo));
                    success = true;
                    break;
                case OFFENSE_TYPE:
                case DEFENSE_TYPE:
                    reverseType(target, effect, changeStatType);
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
    private boolean dealDmg(Card target, Effect effect) {
        boolean success = false;
        if (effect.getEffectInfo() != null && effect.getEffectInfo().size() >= 2 && effect.getEffectInfo().get(0) != null && effect.getEffectInfo().get(1) != null) {
            int dmg = floatObjectToInt(effect.getEffectInfo().get(0));
            TechType techType = TechType.valueOf(effect.getEffectInfo().get(1).toString());
            dmg = DuelManager.getDmgAgainstShields(dmg, techType, target.getCardInfo().getDefenseType());
            target.receiveDMG(dmg);
            success = true;
        }
        return success;
    }

    //-----------//
    //-UTILITIES-//
    //-----------//

    public static boolean validAbilityTarget(AbilityInfo abilityInfo, Card caster, Card target) {
        switch (abilityInfo.getTargets()) {
            default:
                return true;
            case SELF:
                return caster==target;
        }
    }

    public static boolean hasStarter(Card card, AbilityStarter abilityStarter) {
        for (AbilityInfo abilityInfo : card.getCardInfo().getAbilities()) {
            if (abilityInfo.getStarter() == abilityStarter) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasAttribute(Card card, EffectType effectType) { //checks for abilities with "starter=NONE" (old "attributes")
        for (AbilityInfo abilityInfo : card.getCardInfo().getAbilities()) {
            if (abilityInfo.getStarter() == AbilityStarter.NONE) {
                for (Effect effect : abilityInfo.getEffects()) {
                    if (effect.getEffectType() == effectType) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static int getReach(Card card) {
        for (AbilityInfo abilityInfo : card.getCardInfo().getAbilities()) {
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

    public boolean isTheSameAbility(AbilityInfo abilityA, AbilityInfo abilityB) {
        if (abilityA != null && abilityB != null) {
            return (abilityA.getStarter() == abilityB.getStarter()) && isTheSameEffectsList(abilityA.getEffects(), abilityB.getEffects());
        }
        return (abilityA == abilityB);
    }

    public boolean isTheSameEffectsList(ArrayList<Effect> effectsA, ArrayList<Effect> effectsB) {
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

    private void saveEffect(Card target, Effect effect) {
        if (getBattle().getCombatManager().getDuelManager().isActive()) {
            effect.setDuration(effect.getDuration()-1);
        }
        target.addToEffects(effect);
    }

    public static AbilityInfo effectToAbility(ArrayList effectInfo, int effectDuration) { //creates new ability-attribute
        EffectType effectType = EffectType.valueOf(effectInfo.get(1).toString());
        Effect newEffect = new Effect();
        newEffect.setEffectType(effectType);
        ArrayList info = new ArrayList();
        info.addAll(effectInfo);
        if (info.size() >= 4) { info.set(0, effectInfo.get(4)); }
        newEffect.setEffectInfo(info);
        newEffect.setDuration(effectDuration);
        ArrayList<Effect> newAbilityEffects = new ArrayList<>();
        newAbilityEffects.add(newEffect);
        return new AbilityInfo(AbilityStarter.valueOf(effectInfo.get(2).toString()), newAbilityEffects, new ResourcePrice(), AbilityTargets.NONE); //todo targets
    }

    private void reverseType(Card target, Effect effect, EffectTypeSpecifics.ChangeStatType type) {
        boolean success = false;
        ListIterator<Effect> li = target.getEffects().listIterator(target.getEffects().size());
        Effect otherEffect;
        while (!success && li.hasPrevious()) {
            otherEffect = li.previous();
            if (otherEffect != effect && otherEffect.getEffectType() == EffectType.CHANGE_STAT) {
                if (otherEffect.getEffectInfo().get(0) == effect.getEffectInfo().get(0)) {
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

    private static int floatObjectToInt(Object obj) {
        if (obj instanceof Float) {
            float f = (Float) obj;
            return (int) f;
        }
        return 0;
    }

    public Battle getBattle() { return battle; }

}
