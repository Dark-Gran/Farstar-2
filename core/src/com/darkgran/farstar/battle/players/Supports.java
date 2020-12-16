package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.battle.players.cards.Card;
import com.darkgran.farstar.battle.players.cards.CardList;
import com.darkgran.farstar.battle.players.cards.Support;

public class Supports extends CardList implements BattleTicks {

    public Supports() {
        super();
        setupSize();
        clear();
    }

    @Override
    public void setupSize() {
        setMaxSize(6);
    }

    @Override
    public boolean addCard(Card card) {
        if (getCardListMenu() != null && size() < getMaxSize()) {
            Support support = new Support(card.getCardInfo(), card.getPlayer());
            support.setUsed(true);
            add(support);
            getCardListMenu().generateNewToken(support);
            return true;
        }
        return false;
    }

    @Override
    public CardList getCardList() {
        return this;
    }

}
