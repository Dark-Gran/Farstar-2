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
                    //TODO

                    break;
            }
            if (effect.getDuration() > 0) { target.getCard().addToTemps(effect); }
            else { target.getCard().addToPermanents(effect); }
        }
        return success;
    }

    private boolean addStats(Effect effect, Card target, boolean reverse) {
        boolean success = false;

        //TODO

        return success;
    }

    private boolean removeStats(Card card, Card target, boolean reverse) {
        boolean success = false;

        //TODO

        return success;
    }

    public Battle getBattle() { return battle; }

}
