package com.darkgran.farstar.battle;

import com.darkgran.farstar.battle.gui.Token;
import com.darkgran.farstar.battle.players.AbilityInfo;

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

    public boolean executeEffect(Token caster, Token target, int ix) {
        boolean success = false;

        switch (ix) {
            case 0:
                //NO ABILITY
                success = true;
                break;
            case 1:
                //"OLD" STAT UPGRADE
                //TODO
                break;
        }
        return success;
    }

    /*public boolean upgradeShip(Token token, int position) {
        boolean success = false;
        if (ships[position] != null && token.getCard() != null) {
            success = ships[position].applyUpgrade(token.getCard());
            if (success) { junkpile.addCard(token.getCard()); }
        }
        return success;
    }*/

}
