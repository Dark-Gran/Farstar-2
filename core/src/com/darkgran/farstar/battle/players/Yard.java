package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.battle.BattleSettings;

import java.util.ArrayList;

public class Yard extends CardList {

    public Yard(ArrayList<Card> cards) {
        super(cards);
    }

    public Yard() {
        super();
    }

    public Yard(int id) {
        super(id);
    }

    @Override
    public void setupSize() {
        setMaxSize(BattleSettings.YARD_SIZE);
    }

}
