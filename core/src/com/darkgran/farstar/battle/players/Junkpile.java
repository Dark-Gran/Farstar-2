package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.battle.BattleSettings;
import com.darkgran.farstar.battle.players.cards.Card;

//"Junkyard"/"Scrapyard"
public class Junkpile extends CardList {

    public Junkpile() {
        super();
        setupSize();
        clear();
    }

    @Override
    protected void setupSize() {
        setMaxSize(BattleSettings.DECK_SIZE*2);
    }

    @Override
    public boolean addCard(Card card) {
        add(card);
        return true;
    }

}
