package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.battle.gui.CardListMenu;
import com.darkgran.farstar.battle.players.cards.Card;

import java.util.ArrayList;

public abstract class CardList extends ArrayList<Card> {
    private Player player;
    private int maxSize;
    private CardListMenu cardListMenu;

    protected void setupSize() {
        setMaxSize(0);
    }

    public CardList(ArrayList<Card> cards) {
        super();
        setupSize();
        addAll(cards);
    }

    public CardList() {
        super();
        setupSize();
        clear();
        for (int i = 0; i < maxSize; i++) {
            add(new Card(Farstar.CARD_LIBRARY.getCard(2), null));
        }
    }

    public CardList(int id) {
        super();
        setupSize();
        clear();
        for (int i = 0; i < maxSize; i++) {
            add(new Card(Farstar.CARD_LIBRARY.getCard(id), null));
        }
    }

    public void setPlayerOnAll(Player player) {
        setPlayer(player);
        for (Card card : this) {
            card.setPlayer(player);
        }
    }

    public boolean addCard(Card card) {
        if (cardListMenu != null && size() < maxSize) {
            add(card);
            cardListMenu.generateNewToken(card);
            return true;
        }
        return false;
    }

    public void receiveCardListMenu(CardListMenu cardListMenu) {
        this.cardListMenu = cardListMenu;
    }

    public boolean hasSpace() {
        return size() < getMaxSize();
    }

    public int getMaxSize() { return maxSize; }

    public void setMaxSize(int maxSize) { this.maxSize = maxSize; }

    public CardListMenu getCardListMenu() { return cardListMenu; }

    public Player getPlayer() { return player; }

    public void setPlayer(Player player) { this.player = player; }

}
