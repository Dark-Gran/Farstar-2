package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.battle.Battle;
import com.darkgran.farstar.battle.BattleSettings;

import java.util.ArrayList;

public class Yard extends CardList {

    public Yard(int id) {
        super(id);
    }

    public Yard() { //default
        setupSize();
        setCards(new ArrayList<>());
        getCards().add(new Card(Battle.CARD_LIBRARY.getCard(1), null));
        getCards().add(new Card(Battle.CARD_LIBRARY.getCard(2), null));
        getCards().add(new Card(Battle.CARD_LIBRARY.getCard(3), null));
        getCards().add(new Card(Battle.CARD_LIBRARY.getCard(4), null));
        getCards().add(new Card(Battle.CARD_LIBRARY.getCard(5), null));
    }

    @Override
    public void setupSize() {
        setMaxSize(BattleSettings.YARD_SIZE);
    }

}
