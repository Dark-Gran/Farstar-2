package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.battle.AbilityManager;
import com.darkgran.farstar.battle.players.cards.Card;
import com.darkgran.farstar.battle.players.cards.CardList;
import com.darkgran.farstar.battle.players.cards.Support;

import java.util.ArrayList;

public class Supports extends CardList implements BattleTicks {

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

    @Override
    public void setUsedOnAll(boolean used) {
        for (Card card : getCards()) { if (card != null) { card.setUsed(used); } }
    }

    @Override
    public void tickEffectsOnAll(AbilityManager abilityManager) {
        for (Card card : getCards()) { if (card != null) { card.tickEffects(abilityManager); } }
    }

    @Override
    public void checkEffectsOnAll(AbilityManager abilityManager) {
        for (Card card : getCards()) { if (card != null) { card.checkEffects(abilityManager); } }
    }


}
