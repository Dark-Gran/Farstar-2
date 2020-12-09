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

    public boolean executeEffect(Token caster, Token target, Effect effect) {
        boolean success = false;
        if (caster.getCard() != null && target.getCard() != null && effect.getEffectType() != null) {
            switch (effect.getEffectType()) {
                case NONE:
                    success = true;
                    break;
                case ADD_STATS:
                    success = add_stats(caster, target);
                    break;
            }
            if (success) { saveToCard(caster.getCard(), target.getCard(), effect); }
        }
        return success;
    }

    private void saveToCard(Card card, Card target, Effect effect) {
        if (effect.getDuration() > 0) {
            target.addToTemps(effect);
        } else {
            target.addToPermanents(card);
        }
        target.addToHistory(card);
    }

    public Battle getBattle() { return battle; }

    private boolean add_stats(Token caster, Token target) {
        return applyUpgrade(caster.getCard(), target.getCard());
    }

    private boolean applyUpgrade(Card card, Card target) {
        boolean success = false;
        CardInfo upgradeInfo = card.getCardInfo();
        if (upgradeInfo.getCardType() == CardType.UPGRADE) {
            target.getCardInfo().changeOffense(upgradeInfo.getOffense());
            target.getCardInfo().changeDefense(upgradeInfo.getDefense());
            if (upgradeInfo.getOffenseType() != TechType.NONE) { target.getCardInfo().setOffenseType(upgradeInfo.getOffenseType()); }
            if (upgradeInfo.getDefenseType() != TechType.NONE) { target.getCardInfo().setDefenseType(upgradeInfo.getDefenseType()); }
            success = true;
        }
        return success;
    }



}
