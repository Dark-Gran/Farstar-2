package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.battle.Battle;
import com.darkgran.farstar.battle.BattleSettings;
import com.darkgran.farstar.battle.gui.HandMenu;

import java.util.ArrayList;

public class Hand extends CardList{
    private HandMenu handMenu;

    public Hand() {
        setupSize();
        setCards(new ArrayList<Card>());
    }

    public void drawCards(Deck deck, int howMany) {
        for (int i = 0; i < howMany && getCards().size() < Battle.MAX_IN_HAND; i++) {
            Card card = deck.drawCard();
            if (card != null) {
                getCards().add(card);
                if (getCardListMenu() != null) {
                    ((HandMenu) getCardListMenu()).generateNewToken(card);
                }
            }
        }
    }

    @Override
    public void setupSize() {
        setMaxSize(BattleSettings.MAX_IN_HAND);
    }

    public void receiveHandMenu(HandMenu handMenu) {
        this.handMenu = handMenu;
    }

}
