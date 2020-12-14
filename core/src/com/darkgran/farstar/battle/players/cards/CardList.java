package com.darkgran.farstar.battle.players.cards;

import com.darkgran.farstar.battle.Battle;
import com.darkgran.farstar.battle.gui.CardListMenu;
import com.darkgran.farstar.battle.players.Player;

import java.util.ArrayList;

//in-future: cardList could handle using abilities on shipyard/deck/hand (if ever to be added),
//(simply call "checkEffects()" on its Cards (see Fleet/Card))
public abstract class CardList {
    private Player player;
    private ArrayList<Card> cards;
    private int maxSize;
    private CardListMenu cardListMenu;

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
            cards.add(new Card(Battle.CARD_LIBRARY.getCard(2), null));
        }
    }

    public CardList(int id) {
        setupSize();
        cards = new ArrayList<Card>();
        for (int i = 0; i < maxSize; i++) {
            cards.add(new Card(Battle.CARD_LIBRARY.getCard(id), null));
        }
    }

    public void setPlayerOnAll(Player player) {
        setPlayer(player);
        for (Card card : cards) {
            card.setPlayer(player);
        }
    }

    public boolean addCard(Card card) {
        if (cards != null && cardListMenu != null && cards.size() < maxSize) {
            cards.add(card);
            cardListMenu.generateNewToken(card);
            return true;
        }
        return false;
    }

    public void receiveCardListMenu(CardListMenu cardListMenu) { this.cardListMenu = cardListMenu; }

    public ArrayList<Card> getCards() { return cards; }

    public void setCards(ArrayList<Card> cards) { this.cards = cards; }

    public int getMaxSize() { return maxSize; }

    public void setMaxSize(int maxSize) { this.maxSize = maxSize; }

    public CardListMenu getCardListMenu() { return cardListMenu; }

    public Player getPlayer() { return player; }

    public void setPlayer(Player player) { this.player = player; }

}
