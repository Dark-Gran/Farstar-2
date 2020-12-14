package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.battle.players.cards.Mothership;

public class LocalPlayer extends Player {

    public LocalPlayer(byte battleID, int energy, int matter, Mothership ms, Deck deck, Yard yard) {
        super(battleID, energy, matter, ms, deck, yard);
    }

    public LocalPlayer() {
        super();
    }

}
