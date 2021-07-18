package com.darkgran.farstar.cards;

public enum CardRarity {
    IRON("I"),
    BRONZE("B"),
    SILVER("S"),
    GOLD("G");

    private final String acronym;

    CardRarity(String acronym) {
        this.acronym = acronym;
    }

    public String getAcronym() {
        return acronym;
    }
}
