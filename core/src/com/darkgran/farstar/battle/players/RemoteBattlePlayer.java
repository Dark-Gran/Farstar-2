package com.darkgran.farstar.battle.players;

public class RemoteBattlePlayer extends BattlePlayer {

    public RemoteBattlePlayer(byte battleID, int energy, int matter, Mothership ms, Deck deck, Yard yard) {
        super(battleID, energy, matter, ms, deck, yard);
    }

    public RemoteBattlePlayer() {
        super();
    }

}
