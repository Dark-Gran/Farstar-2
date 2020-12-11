package com.darkgran.farstar.battle.players;

import java.util.ArrayList;

public class Supports extends CardList {

    public Supports() {
        setupSize();
        setCards(new ArrayList<>());
    }

    @Override
    public void setupSize() {
        setMaxSize(6);
    }

    @Override
    public boolean addCard(Card card) {
        if (getCards() != null && getCardListMenu() != null && getCards().size() < getMaxSize()) {
            Support support = new Support(card.getCardInfo(), card.getPlayer());
            getCards().add(support);
            getCardListMenu().generateNewToken(support);
            return true;
        }
        return false;
    }


}
