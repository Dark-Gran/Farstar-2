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

    public void addShip(ShipToken ship, int position) {
        ships[position] = ship;
    }

    public float getOffset() { return offset; }

    public void setOffset(float offset) { this.offset = offset; }

    public GlyphLayout getLayout() { return layout; }

}