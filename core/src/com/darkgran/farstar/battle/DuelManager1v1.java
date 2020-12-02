package com.darkgran.farstar.battle;

import com.darkgran.farstar.battle.gui.DuelMenu1v1;
import com.darkgran.farstar.battle.gui.Token;
import com.darkgran.farstar.battle.players.DuelPlayer;

public class DuelManager1v1 extends DuelManager { //1v1 = each Player[] contains only one attacker/defender

    @Override
    public void launchDuel(CombatManager combatManager, Token attacker, Token defender, DuelPlayer[] playersA, DuelPlayer[] playersD) {
        super.launchDuel(combatManager, attacker, defender, playersA, playersD);
    }

    @Override
    public void preparePlayers() {
        super.preparePlayers();
        if (getPlayersA()[0].getBattleID() == 1) {
            setPlayersA_OK(0, ((DuelMenu1v1) getDuelMenu()).getDuelButton1());
            ((DuelMenu1v1) getDuelMenu()).getDuelButton1().setDuelPlayer(getPlayersA()[0]);
            setPlayersD_OK(0, ((DuelMenu1v1) getDuelMenu()).getDuelButton2());
            ((DuelMenu1v1) getDuelMenu()).getDuelButton2().setDuelPlayer(getPlayersD()[0]);
        } else {
            setPlayersA_OK(0, ((DuelMenu1v1) getDuelMenu()).getDuelButton2());
            ((DuelMenu1v1) getDuelMenu()).getDuelButton2().setDuelPlayer(getPlayersA()[0]);
            setPlayersD_OK(0, ((DuelMenu1v1) getDuelMenu()).getDuelButton1());
            ((DuelMenu1v1) getDuelMenu()).getDuelButton1().setDuelPlayer(getPlayersD()[0]);
        }
    }

}
