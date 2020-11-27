package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.battle.BattleSettings;

import java.util.ArrayList;

public class Shipyard extends CardList {

    public Shipyard(ArrayList<Card> cards) {
        super(cards);
    }

    public Shipyard() {
        super();
    }

    public Shipyard(int id) {
        super(id);
    }

    @Override
    public void setupSize() {
        setMaxSize(BattleSettings.YARD_SIZE);
    }

}
