package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.battle.Battle;

import java.util.ArrayList;

public abstract class CardList {
    private final ArrayList<Card> cards;
    private int maxSize;

    public void setupSize() {
        setMaxSize(0);
    }

    public CardList(ArrayList<Card> cards) {
        setupSize();
        this.cards = cards;
    }

    public CardList() {
        setupSize();
        cards = new ArrayList<Card>();
        for (int i = 0; i < maxSize; i++) {
            cards.add(new Card(Battle.CARD_LIBRARY.getCard(2)));
        }
    }

    public CardList(int id) {
        setupSize();
        cards = new ArrayList<Card>();
        for (int i = 0; i < maxSize; i++) {
            cards.add(new Card(Battle.CARD_LIBRARY.getCard(id)));
        }
    }

    public ArrayList<Card> getCards() { return cards; }

    public int getMaxSize() { return maxSize; }

    public void setMaxSize(int maxSize) { this.maxSize = maxSize; }


}
