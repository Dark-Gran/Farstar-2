package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.battle.BattleSettings;

public class Shipyard extends CardList {

    @Override
    public void setupSize() {
        setMaxSize(BattleSettings.YARD_SIZE);
    }

}
