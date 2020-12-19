package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.battle.BattleSettings;
import com.darkgran.farstar.battle.players.cards.Card;

public class Yard extends CardList {

    public Yard(int id) {
        super(id);
    }

    public Yard() { //default
        super();
        setupSize();
        clear();
        add(new Card(Farstar.CARD_LIBRARY.getCard(1), null));
        add(new Card(Farstar.CARD_LIBRARY.getCard(2), null));
        add(new Card(Farstar.CARD_LIBRARY.getCard(3), null));
        add(new Card(Farstar.CARD_LIBRARY.getCard(4), null));
        add(new Card(Farstar.CARD_LIBRARY.getCard(5), null));
    }

    @Override
    protected void setupSize() {
        setMaxSize(BattleSettings.YARD_SIZE);
    }

}
