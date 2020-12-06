package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.battle.BattleSettings;

public class Deck extends CardList {

    public Deck(int id) {
        super(id);
    }

    @Override
    public void setupSize() {
        setMaxSize(BattleSettings.DECK_SIZE);
    }

    public Card drawCard() {
        Card card = getCards().get(0);
        getCards().remove(0);
        return card;
    }

}
