package com.darkgran.farstar.battle;

import com.darkgran.farstar.battle.gui.Token;
import com.darkgran.farstar.battle.players.AbilityInfo;
import com.darkgran.farstar.battle.players.EffectType;

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

    public boolean executeEffect(Token caster, Token target, EffectType effectType) {
        boolean success = false;
        if (caster.getCard() != null && target.getCard() != null && effectType != null) {
            switch (effectType) {
                case NONE:
                    success = true;
                    break;
                case ADD_STATS:
                    success = add_stats(caster, target);
                    break;
            }
        }
        return success;
    }

    private boolean add_stats(Token caster, Token target) { //todo: permanent vs x turns
        return target.getCard().applyUpgrade(caster.getCard());
    }

    public Battle getBattle() { return battle; }

}
