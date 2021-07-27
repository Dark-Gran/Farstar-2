package com.darkgran.farstar.battle.players;

import com.darkgran.farstar.gui.battlegui.Menu;

public class PossibilityInfo {
    private final BattleCard battleCard;
    private final Menu menu;

    public PossibilityInfo(BattleCard battleCard, Menu menu) {
        this.battleCard = battleCard;
        this.menu = menu;
    }

    public BattleCard getCard() {
        return battleCard;
    }

    public Menu getMenu() {
        return menu;
    }

}
