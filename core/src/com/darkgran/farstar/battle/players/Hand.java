package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.battle.Battle;
import com.darkgran.farstar.battle.BattleSettings;

import java.util.ArrayList;

public class Hand extends CardList{

    public Hand() {
        setupSize();
        setCards(new ArrayList<>());
    }

    public void drawCards(Deck deck, int howMany) {
        for (int i = 0; i < howMany && getCards().size() < Battle.MAX_IN_HAND; i++) {
            Card card = deck.drawCard();
            if (card != null) {
                getCards().add(card);
                if (getCardListMenu() != null) {
                    getCardListMenu().generateNewToken(card);
                }
            }
        }
    }

    @Override
    public void setupSize() {
        setMaxSize(BattleSettings.MAX_IN_HAND);
    }

}
