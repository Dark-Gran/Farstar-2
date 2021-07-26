package com.darkgran.farstar.cards;

public enum CardCulture {
    NEUTRAL("N"),
    HUMAN("H"),
    PILGRIM("PIL"), //Missing Card Pictures
    PIRATE("PIR"), //Missing Card Pictures
    TECHNOCRAT("T");

    private final String acronym;

    CardCulture(String acronym) {
        this.acronym = acronym;
    }

    public String getAcronym() {
        return acronym;
    }

    public boolean isHuman(CardCulture culture) {
        return culture == HUMAN || culture == TECHNOCRAT || culture == PILGRIM || culture == PIRATE;
    }
}
