package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.battle.players.cards.Card;
import com.darkgran.farstar.battle.players.cards.CardList;

import static com.darkgran.farstar.battle.BattleSettings.MAX_IN_HAND;

public class Hand extends CardList {

    public Hand() {
        super();
        setupSize();
        clear();
    }

    public void drawCards(Deck deck, int howMany) {
        for (int i = 0; i < howMany && size() < MAX_IN_HAND; i++) {
            Card card = deck.drawCard();
            if (card != null) {
                add(card);
                if (getCardListMenu() != null) {
                    getCardListMenu().generateNewToken(card);
                }
            }
        }
    }

    public void drawCards(int id, int howMany, Player player) {
        for (int i = 0; i < howMany && size() < MAX_IN_HAND; i++) {
            Card card = new Card(Farstar.CARD_LIBRARY.getCard(id), player);
            if (card != null) {
                add(card);
                if (getCardListMenu() != null) {
                    getCardListMenu().generateNewToken(card);
                }
            }
        }
    }

    @Override
    public void setupSize() {
        setMaxSize(MAX_IN_HAND);
    }

}
