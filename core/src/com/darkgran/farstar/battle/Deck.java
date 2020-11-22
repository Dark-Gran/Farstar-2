package com.darkgran.farstar.battle;

import java.util.ArrayList;

public class Deck {
    private final ArrayList<Card> cards;

    public Deck(ArrayList<Card> cards) {
        this.cards = cards;
    }

    public Deck() {
            cards = new ArrayList<>(); //Todo: default deck
    }

    public ArrayList<Card> getCards() { return cards; }

}
