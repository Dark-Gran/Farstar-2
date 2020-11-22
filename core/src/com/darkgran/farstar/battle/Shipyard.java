package com.darkgran.farstar.battle;

import java.util.ArrayList;

public class Shipyard {
    private final ArrayList<Card> cards;

    public Shipyard(ArrayList<Card> cards) {
        this.cards = cards;
    }

    public Shipyard() { //generates basic yard
        cards = new ArrayList<>();
        for (int i = 0; i < BattleSettings.YARD_SIZE; i++) {
            cards.add(new Card(BattleManager.CARD_LIBRARY.getCard(1)));
        }
    }

    public ArrayList<Card> getCards() { return cards; }
}
