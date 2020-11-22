package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.battle.Battle;
import com.darkgran.farstar.battle.BattleSettings;

import java.util.ArrayList;

public class Deck {
    private final ArrayList<Card> cards;

    public Deck(ArrayList<Card> cards) {
        this.cards = cards;
    }

    public Deck() { //generates basic yard
        cards = new ArrayList<>();
        for (int i = 0; i < BattleSettings.DECK_SIZE; i++) {
            cards.add(new Card(Battle.CARD_LIBRARY.getCard(2)));
        }
    }

    public ArrayList<Card> getCards() { return cards; }

    public Card drawCard() {
        Card card = cards.get(0);
        cards.remove(0);
        return card;
    }

}
