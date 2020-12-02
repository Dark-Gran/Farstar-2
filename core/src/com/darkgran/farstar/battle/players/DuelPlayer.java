package com.darkgran.farstar.battle.players;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class DuelPlayer extends Player {
    private Actor duelButton;
    private boolean ready;

    public DuelPlayer(byte battleID, int energy, int matter, Mothership ms, Deck deck, Shipyard shipyard) {
        super(battleID, energy, matter, ms, deck, shipyard);
    }

    public Actor getDuelButton() {
        return duelButton;
    }

    public void setDuelButton(Actor duelButton) {
        this.duelButton = duelButton;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }
}
