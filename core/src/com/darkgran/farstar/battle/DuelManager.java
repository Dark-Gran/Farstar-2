package com.darkgran.farstar.battle;

import com.darkgran.farstar.battle.gui.DuelMenu;
import com.darkgran.farstar.battle.gui.Token;
import com.darkgran.farstar.battle.players.DuelPlayer;

public abstract class DuelManager {
    private DuelMenu duelMenu;
    private boolean active = false;
    private Token attacker;
    private Token defender;
    private DuelPlayer[] playersA;
    private DuelPlayer[] playersD;

    public void launchDuel(Token attacker, Token defender, DuelPlayer[] playersA, DuelPlayer[] playersD) {
        active = true;
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
        System.out.println("OK");
        duelMenu.removeCancel();
    }

    public void endDuel() {
        active = false;
    }

    //TODO click-responses

    public void receiveDuelMenu(DuelMenu duelMenu) { this.duelMenu = duelMenu; }

    public DuelMenu getDuelMenu() { return duelMenu; }

    public boolean isActive() { return active; }

    public void setActive(boolean active) { this.active = active; }

    public Token getAttacker() { return attacker; }

    public Token getDefender() { return defender; }

    public DuelPlayer[] getPlayersA() { return playersA; }

    public DuelPlayer[] getPlayersD() { return playersD; }


}
