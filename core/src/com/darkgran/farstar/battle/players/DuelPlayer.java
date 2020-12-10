package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.battle.gui.DuelOK;

//wrapper
public class DuelPlayer {
    private final Player player;
    private DuelOK duelOK;
    private boolean ready;

    public DuelPlayer(Player player) {
        this.player = player;
    }

    public DuelOK getDuelButton() {
        return duelOK;
    }

    public void setDuelOK(DuelOK duelOK) {
        this.duelOK = duelOK;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public Player getPlayer() { return player; }
}
