package com.darkgran.farstar.battle;

import com.darkgran.farstar.gui.battlegui.CombatMenu1V1;

public class CombatManager1v1 extends CombatManager {

    CombatManager1v1(Battle battle, DuelManager duelManager) {
        super(battle, duelManager);
    }

    @Override
    void preparePlayers() {
        super.preparePlayers();
        if (getPlayersA()[0].getPlayer().getBattleID() == 1) {
            setPlayersA_OK(0, ((CombatMenu1V1) getCombatMenu()).getDuelButton1());
            ((CombatMenu1V1) getCombatMenu()).getDuelButton1().setDuelPlayer(getPlayersA()[0]);
            setPlayersD_OK(0, ((CombatMenu1V1) getCombatMenu()).getDuelButton2());
            ((CombatMenu1V1) getCombatMenu()).getDuelButton2().setDuelPlayer(getPlayersD()[0]);
        } else {
            setPlayersA_OK(0, ((CombatMenu1V1) getCombatMenu()).getDuelButton2());
            ((CombatMenu1V1) getCombatMenu()).getDuelButton2().setDuelPlayer(getPlayersA()[0]);
            setPlayersD_OK(0, ((CombatMenu1V1) getCombatMenu()).getDuelButton1());
            ((CombatMenu1V1) getCombatMenu()).getDuelButton1().setDuelPlayer(getPlayersD()[0]);
        }
    }

}
