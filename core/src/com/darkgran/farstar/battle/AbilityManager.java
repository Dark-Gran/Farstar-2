package com.darkgran.farstar.battle;

import com.darkgran.farstar.battle.players.*;
import com.darkgran.farstar.battle.players.abilities.*;

import java.util.ArrayList;
import java.util.ListIterator;

import static com.darkgran.farstar.battle.players.abilities.EffectTypeSpecifics.ChangeStatType.DEFENSE_TYPE;
import static com.darkgran.farstar.battle.players.abilities.EffectTypeSpecifics.ChangeStatType.OFFENSE_TYPE;

public class AbilityManager { //TODO 1. guard 2. reach
    private final Battle battle;

    public AbilityManager(Battle battle) {
        this.battle = battle;
    }

    public boolean playAbility(Card caster, Card target, int abilityIX) {
        boolean success = false;
        if (caster != null && caster.getCardInfo().getAbilities() != null && target != null) {
            AbilityInfo ability = caster.getCardInfo().getAbilities().get(abilityIX);
            if (ability != null && ability.getEffects() != null) {
                for (int i = 0; i < ability.getEffects().size(); i++) {
                    if (ability.getEffects().get(i) != null) {
                        if (!success) { success = executeEffect(target, ability.getEffects().get(i), false); }
                        else { executeEffect(target, ability.getEffects().get(i), false); }
                    }
                }
                if (success) { target.addToHistory(caster, abilityIX); }
            }
        }
        return success;
    }

    public boolean executeEffect(Card target, Effect effect, boolean reverse) {
        boolean success = false;
        if (effect.getEffectType() != null) {
            switch (effect.getEffectType()) {
                case NONE:
                    success = true;
                    break;
                case CHANGE_STAT:
                    if (!reverse) { success = changeStat(target, effect, reverse); }
                    else { success = reverseStat(target, effect, reverse); }
                    break;
            }
        }
        return success;
    }

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
                        if (!target.getCardInfo().getAbilities().contains(newAbility)) {
                            target.getCardInfo().addAbility(newAbility);
                        } else {
                            dontSaveEffect = true;
                        }
                    }
                    success = true;
                    break;
            }
            if (!reverse && !dontSaveEffect) {
                target.addToEffects(InstanceFactory.instanceEffect(effect));
            }
        }
        return success;
    }

    private boolean reverseStat(Card target, Effect effect, boolean reverse) {
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
                    if (effect.getEffectInfo().get(2) != null) {
                        target.getCardInfo().getAbilities().remove(effectToAbility(effect.getEffectInfo(), 0));
                        success = true;
                    }
                    break;
            }
            if (!reverse) {
                target.addToEffects(InstanceFactory.instanceEffect(effect));
            }
        }
        return success;
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

    private static AbilityInfo effectToAbility(ArrayList effectInfo, int effectDuration) { //creates new ability-attribute
        EffectType effectType = EffectType.valueOf(effectInfo.get(1).toString());
        Effect newEffect = new Effect();
        newEffect.setEffectType(effectType);
        newEffect.setEffectInfo(null);
        newEffect.setDuration(effectDuration);
        ArrayList<Effect> newAbilityEffects = new ArrayList<>();
        newAbilityEffects.add(newEffect);
        return new AbilityInfo(AbilityStarter.valueOf(effectInfo.get(2).toString()), newAbilityEffects);
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

    public Battle getBattle() { return battle; }

    private static int floatObjectToInt(Object obj) {
        float f = (Float) obj;
        return (int) f;
    }



}
