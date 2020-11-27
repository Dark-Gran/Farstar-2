package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.battle.Battle;
import com.darkgran.farstar.battle.gui.TokenMenu;

import java.util.ArrayList;

public abstract class CardList {
    private ArrayList<Card> cards;
    private int maxSize;
    private TokenMenu tokenMenu;

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

    public void receiveTokenMenu(TokenMenu tokenMenu) { this.tokenMenu = tokenMenu; }

    public ArrayList<Card> getCards() { return cards; }

    public void setCards(ArrayList<Card> cards) { this.cards = cards; }

    public int getMaxSize() { return maxSize; }

    public void setMaxSize(int maxSize) { this.maxSize = maxSize; }

    public TokenMenu getTokenMenu() { return tokenMenu; }


}
