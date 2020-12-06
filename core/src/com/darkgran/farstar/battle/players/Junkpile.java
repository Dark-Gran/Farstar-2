package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.battle.BattleSettings;

import java.util.ArrayList;

//"Junkyard"/"Scrapyard"
public class Junkpile extends CardList {

    public Junkpile() {
        setupSize();
        setCards(new ArrayList<Card>());
    }

    @Override
    public void setupSize() {
        setMaxSize(BattleSettings.DECK_SIZE*2);
    }

    public void addCard(Card card) {
        getCards().add(card);
        System.out.println(getCards());
    }

}
