package com.darkgran.farstar.battle.gui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.darkgran.farstar.battle.players.Fleet;
import com.darkgran.farstar.util.SimpleBox2;

public class FleetMenu extends SimpleBox2 { //not a TokenMenu (Array vs ArrayList)
    private GlyphLayout layout = new GlyphLayout();
    private float offset;
    private final Fleet fleet;
    private Token[] ships = new Token[7];

    public FleetMenu(Fleet fleet, float x, float y, float width, float height) {
        setX(x);
        setY(y);
        setWidth(width);
        setHeight(height);
        this.fleet = fleet;
        fleet.receiveFleetMenu(this);
        setupOffset();
    }

    public void setupOffset() {
        String res = "Battlestation";
        layout.setText(new BitmapFont(), res);
        offset = layout.width;
    }

    public void addShip(Token ship, int position) {
        ships[position] = ship;
    }

    public float getOffset() { return offset; }

    public void setOffset(float offset) { this.offset = offset; }

    public GlyphLayout getLayout() { return layout; }

    public Fleet getFleet() { return fleet; }

}