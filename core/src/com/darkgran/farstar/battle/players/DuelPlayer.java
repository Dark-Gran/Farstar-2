package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.battle.gui.DuelOK;

public class DuelPlayer extends Player {
    private DuelOK duelOK;
    private boolean ready;

    public DuelPlayer(byte battleID, int energy, int matter, Mothership ms, Deck deck, Shipyard shipyard) {
        super(battleID, energy, matter, ms, deck, shipyard);
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
}
