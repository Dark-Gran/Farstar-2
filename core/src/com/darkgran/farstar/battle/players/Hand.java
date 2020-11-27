package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.battle.Battle;
import com.darkgran.farstar.battle.BattleSettings;
import com.darkgran.farstar.battle.gui.HandMenu;

public class Hand extends CardList{
    private HandMenu handMenu;

    @Override
    public void setupSize() {
        setMaxSize(BattleSettings.MAX_IN_HAND);
    }

    public void drawCards(Deck deck, int howMany) {
        for (int i = 0; i < howMany && getCards().size() < Battle.MAX_IN_HAND; i++) {
            Card card = deck.drawCard();
            getCards().add(card);
            if (handMenu != null) {
                handMenu.generateNewCard(card);
            }
        }
    }

    public void receiveHandMenu(HandMenu handMenu) {
        this.handMenu = handMenu;
    }

}
