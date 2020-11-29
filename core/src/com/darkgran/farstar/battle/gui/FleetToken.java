package com.darkgran.farstar.battle.gui;

import com.darkgran.farstar.battle.players.Card;

public class FleetToken extends Token { //TODO
    private FleetMenu fleetMenu;

    public FleetToken(Card card, float x, float y, BattleStage battleStage, TokenMenu tokenMenu) {
        super(card, x, y, battleStage, tokenMenu);
    }

    public FleetMenu getFleetMenu() { return fleetMenu; }

    public void setFleetMenu(FleetMenu fleetMenu) { this.fleetMenu = fleetMenu; }

}
