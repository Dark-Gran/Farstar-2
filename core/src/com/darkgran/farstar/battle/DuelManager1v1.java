package com.darkgran.farstar.battle;

import com.darkgran.farstar.battle.gui.Token;
import com.darkgran.farstar.battle.players.Player;

public class DuelManager1v1 extends DuelManager { //1v1 = each Player[] contains only one attacker/defender

    @Override
    public void launchDuel(Token attacker, Token defender, Player[] playersA, Player[] playersD) {
        super.launchDuel(attacker, defender, playersA, playersD);
    }

    @Override
    public void endDuel() {
        super.endDuel();
    }

}
