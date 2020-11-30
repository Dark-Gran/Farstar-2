package com.darkgran.farstar.battle.gui;

import com.darkgran.farstar.battle.players.Card;

public class FleetToken extends AnchoredToken {
    private FleetMenu fleetMenu;

    public FleetToken(Card card, float x, float y, BattleStage battleStage, TokenMenu tokenMenu, FleetMenu fleetMenu) {
        super(card, x, y, battleStage, tokenMenu);
        this.fleetMenu = fleetMenu;
        setDragger(new ManagedDragger(this, battleStage.getBattleScreen().getBattle().getRoundManager(), true));
        this.addListener(getDragger().getInputListener());
    }

    public FleetMenu getFleetMenu() { return fleetMenu; }

    public void setFleetMenu(FleetMenu fleetMenu) { this.fleetMenu = fleetMenu; }

}
