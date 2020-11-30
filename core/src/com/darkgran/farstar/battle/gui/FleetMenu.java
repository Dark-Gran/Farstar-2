package com.darkgran.farstar.battle.gui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.darkgran.farstar.battle.players.Card;
import com.darkgran.farstar.battle.players.Fleet;
import com.darkgran.farstar.util.SimpleBox2;

public class FleetMenu extends SimpleBox2 implements DropTarget { //not a TokenMenu (Array vs ArrayList)
    private final BattleStage battleStage;
    private GlyphLayout layout = new GlyphLayout();
    private float offset;
    private final Fleet fleet;
    private FleetToken[] ships = new FleetToken[7];

    public FleetMenu(Fleet fleet, float x, float y, float width, float height, BattleStage battleStage) {
        this.battleStage = battleStage;
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

    public void addShip(Card card, int position) {
        ships[position] = new FleetToken(card, getX()+offset*(position+1), getY(), battleStage, null, this);
    }

    public void removeShip(int position) {
        ships[position] = null;
    }

    public float getOffset() { return offset; }

    public void setOffset(float offset) { this.offset = offset; }

    public GlyphLayout getLayout() { return layout; }

    public Fleet getFleet() { return fleet; }

    public Token[] getShips() { return ships; }

}