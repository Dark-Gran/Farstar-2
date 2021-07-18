package com.darkgran.farstar.battle.players;

public class LocalBattlePlayer extends BattlePlayer {

    public LocalBattlePlayer(byte battleID, int energy, int matter, Mothership ms, Deck deck, Yard yard) {
        super(battleID, energy, matter, ms, deck, yard);
    }

    public LocalBattlePlayer() {
        super();
    }

}
