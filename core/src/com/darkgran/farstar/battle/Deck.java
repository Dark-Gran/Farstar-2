package com.darkgran.farstar.battle;

import java.util.ArrayList;

public class Deck {
    private final ArrayList<Card> cards;

    public Deck(ArrayList<Card> cards) {
        this.cards = cards;
    }

    public Deck() { //generates basic yard
        cards = new ArrayList<>();
        for (int i = 0; i < BattleSettings.DECK_SIZE; i++) {
            cards.add(new Card(BattleManager.CARD_LIBRARY.getCard(2)));
        }
    }

    public ArrayList<Card> getCards() { return cards; }

}
