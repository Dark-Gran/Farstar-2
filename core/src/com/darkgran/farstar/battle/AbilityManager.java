package com.darkgran.farstar.battle;

import com.darkgran.farstar.battle.gui.Token;
import com.darkgran.farstar.battle.players.AbilityInfo;
import com.darkgran.farstar.battle.players.Effect;

public class AbilityManager { //TODO remove statics, move abilities from Card to here, undoEffects
    private final Battle battle;

    public AbilityManager(Battle battle) {
        this.battle = battle;
    }

    public static boolean playAbility(Token caster, Token target) {
        boolean success = false;
        if (caster != null && target != null) {
            AbilityInfo ability = caster.getCard().getCardInfo().getAbility();
            if (ability != null && ability.getEffects() != null) {
                for (int i = 0; i < ability.getEffects().size(); i++) {
                    if (!success) {
                        success = executeEffect(caster, target, ability.getEffects().get(i));
                    } else {
                        executeEffect(caster, target, ability.getEffects().get(i));
                    }
                }
            }
        }
        return success;
    }

    public static boolean executeEffect(Token caster, Token target, Effect effect) {
        boolean success = false;
        if (caster.getCard() != null && target.getCard() != null && effect.getEffectType() != null) {
            switch (effect.getEffectType()) {
                case NONE:
                    success = true;
                    break;
                case ADD_STATS:
                    success = add_stats(caster, target, effect);
                    break;
            }
        }
        return success;
    }

    private static boolean add_stats(Token caster, Token target, Effect effect) {
        return target.getCard().applyUpgrade(caster.getCard(), effect);
    }

    public Battle getBattle() { return battle; }

}
