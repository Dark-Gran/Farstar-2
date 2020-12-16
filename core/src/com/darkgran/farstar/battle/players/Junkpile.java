package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.battle.BattleSettings;
import com.darkgran.farstar.battle.players.cards.Card;
import com.darkgran.farstar.battle.players.cards.CardList;

import java.util.ArrayList;

//"Junkyard"/"Scrapyard"
public class Junkpile extends CardList {

    public Junkpile() {
        super();
        setupSize();
        clear();
    }

    @Override
    public void setupSize() {
        setMaxSize(BattleSettings.DECK_SIZE*2);
    }

    @Override
    public boolean addCard(Card card) {
        add(card);
        return true;
    }

}
