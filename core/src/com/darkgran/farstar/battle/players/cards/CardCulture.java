package com.darkgran.farstar.battle.players.cards;

public enum CardCulture {
    NEUTRAL("N"),
    HUMAN("H");

    private final String acronym;

    CardCulture(String acronym) {
        this.acronym = acronym;
    }

    public String getAcronym() {
        return acronym;
    }

    public boolean isHuman(CardCulture culture) {
        return culture == HUMAN;
    }
}
