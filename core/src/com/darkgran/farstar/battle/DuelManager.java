package com.darkgran.farstar.battle;

import com.darkgran.farstar.battle.gui.DuelMenu;
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
        duelMenu.addActor(playersA[0].getDuelButton());
    }

    public void preparePlayers() {
        for (DuelPlayer player : playersA) { player.setReady(false); }
        for (DuelPlayer player : playersD) { player.setReady(false); }
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

    public void receiveDuelMenu(DuelMenu duelMenu) { this.duelMenu = duelMenu; }

    public DuelMenu getDuelMenu() { return duelMenu; }

    public boolean isActive() { return active; }

    public void setActive(boolean active) { this.active = active; }

    public Token getAttacker() { return attacker; }

    public Token getDefender() { return defender; }

    public DuelPlayer[] getPlayersA() { return playersA; }

    public DuelPlayer[] getPlayersD() { return playersD; }


}
