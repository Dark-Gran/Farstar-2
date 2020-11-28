package com.darkgran.farstar.battle.gui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.darkgran.farstar.battle.players.Fleet;
import com.darkgran.farstar.util.SimpleVector2;

public class FleetMenu extends SimpleVector2 { //not a TokenMenu (Array vs ArrayList)
    private GlyphLayout layout = new GlyphLayout();
    private float offset;
    private final Fleet fleet;
    private ShipToken[] ships = new ShipToken[7];

    public FleetMenu(Fleet fleet, float x, float y) {
        setX(x);
        setY(y);
        this.fleet = fleet;
        fleet.receiveFleetMenu(this);
        setupOffset();
    }

    public void setupOffset() {
        String res = "Battlestation";
        layout.setText(new BitmapFont(), res);
        offset = layout.width;
    }

    public void addShip(ShipToken ship) {
        if (getFreePosition() != 7) {
            ships[getFreePosition()] = ship;
        }
    }

    private int getFreePosition() {
        if (ships[3] == null) {
            return 3;
        } else if (ships[2] == null) {
            return 2;
        } else if (ships[4] == null) {
            return 4;
        } else if (ships[1] == null) {
            return 1;
        } else if (ships[5] == null) {
            return 5;
        } else if (ships[0] == null) {
            return 0;
        } else if (ships[6] == null) {
            return 6;
        } else {
            return 7;
        }
    }

    public float getOffset() { return offset; }

    public void setOffset(float offset) { this.offset = offset; }

    public GlyphLayout getLayout() { return layout; }

}