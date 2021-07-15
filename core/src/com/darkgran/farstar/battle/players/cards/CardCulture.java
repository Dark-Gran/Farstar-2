package com.darkgran.farstar.battle.players.cards;

public enum CardCulture {
    NEUTRAL, HUMAN;

    public boolean isHuman(CardCulture culture) {
        return culture == HUMAN;
    }
}
