package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.battle.BattleSettings;
import com.darkgran.farstar.cards.CardLibrary;

public class Hand extends CardList {

    public Hand() {
        super();
        setupSize();
        clear();
    }

    public void getNewCards(Deck deck, int howMany) {
        for (int i = 0; i < howMany && size() < BattleSettings.getInstance().MAX_IN_HAND; i++) {
            BattleCard battleCard = deck.drawCard();
            if (battleCard != null) {
                add(battleCard);
                if (getCardListMenu() != null) {
                    getCardListMenu().generateNewToken(battleCard);
                }
            }
        }
    }

    public void getNewCards(int id, int howMany, BattlePlayer battlePlayer) {
        for (int i = 0; i < howMany && size() < BattleSettings.getInstance().MAX_IN_HAND; i++) {
            BattleCard battleCard = new BattleCard(CardLibrary.getInstance().getCard(id), battlePlayer);
            add(battleCard);
            if (getCardListMenu() != null) {
                getCardListMenu().generateNewToken(battleCard);
            }
        }
    }

    @Override
    protected void setupSize() {
        setMaxSize(BattleSettings.getInstance().MAX_IN_HAND);
    }

}
