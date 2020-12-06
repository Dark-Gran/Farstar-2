package com.darkgran.farstar.battle.players;

import java.util.ArrayList;

//"Junkyard"/"Scrapyard"
public class Junkpile extends CardList {

    public Junkpile() {
        setupSize();
        setCards(new ArrayList<Card>());
    }

    @Override
    public void setupSize() {
        setMaxSize(0); //TODO
    }

}
