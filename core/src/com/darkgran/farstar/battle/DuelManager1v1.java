package com.darkgran.farstar.battle;

import com.darkgran.farstar.battle.gui.DuelMenu1v1;
import com.darkgran.farstar.battle.gui.Token;
import com.darkgran.farstar.battle.players.DuelPlayer;

public class DuelManager1v1 extends DuelManager { //1v1 = each Player[] contains only one attacker/defender

    @Override
    public void launchDuel(Token attacker, Token defender, DuelPlayer[] playersA, DuelPlayer[] playersD) {
        super.launchDuel(attacker, defender, playersA, playersD);
    }

    @Override
    public void preparePlayers() {
        super.preparePlayers();
        if (getPlayersA()[0].getBattleID() == 1) {
            getPlayersA()[0].setDuelButton(((DuelMenu1v1) getDuelMenu()).getDuelButton1());
            getPlayersD()[0].setDuelButton(((DuelMenu1v1) getDuelMenu()).getDuelButton2());
        } else {
            getPlayersA()[0].setDuelButton(((DuelMenu1v1) getDuelMenu()).getDuelButton2());
            getPlayersD()[0].setDuelButton(((DuelMenu1v1) getDuelMenu()).getDuelButton1());
        }
    }

}
