package com.darkgran.farstar.battle.players.ai;

import com.darkgran.farstar.battle.players.Deck;
import com.darkgran.farstar.battle.players.Player;
import com.darkgran.farstar.battle.players.Yard;
import com.darkgran.farstar.battle.players.cards.Mothership;

public class Automaton extends Player {

    public Automaton(byte battleID, int energy, int matter, Mothership ms, Deck deck, Yard yard) {
        super(battleID, energy, matter, ms, deck, yard);
    }

    public Automaton() {
        super();
    }

}
