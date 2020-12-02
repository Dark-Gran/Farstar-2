package com.darkgran.farstar.battle;

import com.darkgran.farstar.battle.gui.DuelMenu;
import com.darkgran.farstar.battle.gui.DuelOK;
import com.darkgran.farstar.battle.gui.Token;
import com.darkgran.farstar.battle.players.DuelPlayer;

public abstract class DuelManager {
    private CombatManager combatManager;
    private DuelMenu duelMenu;
    private boolean active = false;
    private Token attacker;
    private Token defender;
    private DuelPlayer[] playersA;
    private DuelPlayer[] playersD;

    public void launchDuel(CombatManager combatManager, Token attacker, Token defender, DuelPlayer[] playersA, DuelPlayer[] playersD) {
        active = true;
        this.combatManager = combatManager;
        this.attacker = attacker;
        this.defender = defender;
        this.playersA = playersA;
        this.playersD = playersD;
        preparePlayers();
        duelMenu.addCancel();
        duelMenu.addOK(this.playersA[0].getDuelButton());
    }

    public void preparePlayers() {
        for (DuelPlayer player : playersA) { player.setReady(false); }
        for (DuelPlayer player : playersD) { player.setReady(false); }
    }

    public void OK(DuelOK duelOK) {
        System.out.println("OK"); //TODO
    }

    public void cancelDuel() {
        endDuel();
    }

    public void endDuel() {
        duelMenu.removeCancel();
        duelMenu.removeAllOKs();
        active = false;
        combatManager.afterDuel();
    }

    public void setPlayersA_OK(int ix, DuelOK duelOK) {
        if (playersA[ix] != null) { playersA[ix].setDuelOK(duelOK); }
    }

    public void setPlayersD_OK(int ix, DuelOK duelOK) {
        if (playersA[ix] != null) { playersD[ix].setDuelOK(duelOK); }
    }

    public void receiveDuelMenu(DuelMenu duelMenu) { this.duelMenu = duelMenu; }

    public DuelMenu getDuelMenu() { return duelMenu; }

    public boolean isActive() { return active; }

    public void setActive(boolean active) { this.active = active; }

    public Token getAttacker() { return attacker; }

    public Token getDefender() { return defender; }

    public DuelPlayer[] getPlayersA() { return playersA; }

    public DuelPlayer[] getPlayersD() { return playersD; }


}
