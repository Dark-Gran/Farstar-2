package com.darkgran.farstar.battle.gui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.darkgran.farstar.battle.players.Shipyard;
import com.darkgran.farstar.util.SimpleVector2;

import java.util.ArrayList;

public class YardMenu extends SimpleVector2 {
    private ArrayList<YardToken> yardTokens = new ArrayList<>();
    private final Shipyard shipyard; //getCards() ArrayList<Card>
    private boolean onTop = false;
    private boolean visible = false;

    public YardMenu(Shipyard shipyard, boolean onTop, float x, float y) {
        this.shipyard = shipyard;
        this.onTop = onTop;
        setX(x);
        setY(y);
        generateTokens();
    }

    private void generateTokens() {
        yardTokens.clear();
        String res = "Battlestation";
        GlyphLayout layout = new GlyphLayout();
        layout.setText(new BitmapFont(), res);
        for (int i = 0; i < shipyard.getCards().size(); i++) {
            float offsetY = onTop ? -layout.height*(5+i*5) : layout.height*(5+i*5);
            yardTokens.add(new YardToken(shipyard.getCards().get(i), getX(), getY()+offsetY));
        }
    }

    public void switchVisibility() { visible = !visible; }

    public boolean isVisible() { return visible; }

    public void setVisible(boolean visibility) { this.visible = visible; }

    public Shipyard getShipyard() { return shipyard; }

    public ArrayList<YardToken> getYardTokens() { return yardTokens; }



}
