package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.battle.Battle;

import java.util.ArrayList;

public class Hand {
    private ArrayList<Card> cards = new ArrayList<>();

    public void drawCards(Deck deck, int howMany) {
        for (int i = 0; i < howMany && cards.size() < Battle.MAX_CARDS; i++) {
            cards.add(deck.drawCard());
        }
    }
}
