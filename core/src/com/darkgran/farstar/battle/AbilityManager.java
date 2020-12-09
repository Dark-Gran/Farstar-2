package com.darkgran.farstar.battle;

import com.darkgran.farstar.battle.gui.Token;
import com.darkgran.farstar.battle.players.*;

public class AbilityManager {
    private final Battle battle;

    public AbilityManager(Battle battle) {
        this.battle = battle;
    }

    public boolean playAbility(Token caster, Token target) {
        boolean success = false;
        if (caster != null && target != null) {
            AbilityInfo ability = caster.getCard().getCardInfo().getAbility();
            if (ability != null && ability.getEffects() != null) {
                for (int i = 0; i < ability.getEffects().size(); i++) {
                    if (!success) { success = executeEffect(target, ability.getEffects().get(i), false); }
                    else { executeEffect(target, ability.getEffects().get(i), false); }
                }
                if (success) { target.getCard().addToHistory(caster.getCard()); }
            }
        }
        return success;
    }

    public boolean executeEffect(Token target, Effect effect, boolean reverse) {
        boolean success = false;
        if (target.getCard() != null && effect.getEffectType() != null) {
            switch (effect.getEffectType()) {
                case NONE:
                    success = true;
                    break;
                case ADD_STATS:
                    if (!reverse) { success = addStat(target.getCard(), effect); }
                    else { success = removeStat(target.getCard(), effect); }
                    break;
                case REMOVE_STATS:
                    if (!reverse) { success = removeStat(target.getCard(), effect); }
                    else { success = addStat(target.getCard(), effect); }
                    break;
            }
            if (effect.getDuration() > 0) { target.getCard().addToTemps(effect); }
            else { target.getCard().addToPermanents(effect); }
        }
        return success;
    }

    private boolean addStat(Card target, Effect effect) {
        boolean success = false;
        if (effect.getEffectInfo() != null && effect.getEffectInfo().size() >= 2) {
            if (effect.getEffectInfo().get(1) != null) {
                switch (effect.getEffectInfo().get(0).toString()) {
                    case "OFFENSE":
                        target.getCardInfo().changeOffense(floatObjectToInt(effect.getEffectInfo().get(1)));
                        success = true;
                        break;
                }
            }

            /*target.getCardInfo().changeDefense(0);
            if (upgradeInfo.getOffenseType() != TechType.NONE) {
                target.getCardInfo().setOffenseType(TechType.INFERIOR);
            }
            if (upgradeInfo.getDefenseType() != TechType.NONE) {
                target.getCardInfo().setDefenseType(TechType.INFERIOR);
            }
            */
        }
        return success;
    }

    private boolean removeStat(Card target, Effect effect) {
        boolean success = false;

        //TODO

        return success;
    }

    public Battle getBattle() { return battle; }

    private static int floatObjectToInt(Object obj) {
        float f = (Float) obj;
        return (int) f;
    }

}
