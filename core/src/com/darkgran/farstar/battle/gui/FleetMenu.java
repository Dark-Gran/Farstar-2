package com.darkgran.farstar.battle.gui;

import com.darkgran.farstar.battle.players.Card;
import com.darkgran.farstar.battle.players.Fleet;
import com.darkgran.farstar.battle.players.Player;

//uses Array and CardList remains null! (unlike TokenMenu)
public class FleetMenu extends BaseMenu implements DropTarget {
    private final Fleet fleet;
    private FleetToken[] ships = new FleetToken[7];

    public FleetMenu(Fleet fleet, float x, float y, float width, float height, BattleStage battleStage, Player player) {
        super(x, y, false, battleStage, player);
        setX(x);
        setY(y);
        setWidth(width);
        setHeight(height);
        this.fleet = fleet;
        fleet.receiveFleetMenu(this);
        setupOffset();
    }

    public void addShip(Card card, int position) {
        ships[position] = new FleetToken(card, getX()+getOffset()*(position+1), getY(), getBattleStage(), null, this);
    }

    public void removeShip(int position) {
        ships[position] = null;
    }

    public Fleet getFleet() { return fleet; }

    public Token[] getShips() { return ships; }

}