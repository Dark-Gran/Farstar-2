package com.darkgran.farstar.battle;

import java.util.ArrayList;

public class Shipyard {
    private final ArrayList<Card> cards;

    public Shipyard(ArrayList<Card> cards) {
        this.cards = cards;
    }

    public Shipyard() {
        cards = new ArrayList<>(); //Todo: default shipyard
    }

    public ArrayList<Card> getCards() { return cards; }
}
