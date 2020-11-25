package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.battle.Battle;
import com.darkgran.farstar.battle.gui.HandMenu;

import java.util.ArrayList;

public class Hand {
    private ArrayList<Card> cards = new ArrayList<>();
    private HandMenu handMenu;

    public void drawCards(Deck deck, int howMany) {
        for (int i = 0; i < howMany && cards.size() < Battle.MAX_CARDS; i++) {
            Card card = deck.drawCard();
            cards.add(card);
            if (handMenu != null) {
                handMenu.generateNewCard(card);
            }
        }
    }

    public void receiveHandMenu(HandMenu handMenu) {
        this.handMenu = handMenu;
    }

    public ArrayList<Card> getCards() { return cards; }

    public void setCards(ArrayList<Card> cards) { this.cards = cards; }

}
