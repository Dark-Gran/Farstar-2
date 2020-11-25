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
    private boolean visible = true;

    public YardMenu(Shipyard shipyard, boolean onTop, float x, float y) {
        this.shipyard = shipyard;
        this.onTop = onTop;
        setX(x);
        setY(y);
        generateTokens();
    }

    public void generateTokens() {
        String res = "Battlestation";
        GlyphLayout layout = new GlyphLayout();
        layout.setText(new BitmapFont(), res);
        for (int i = 0; i < shipyard.getCards().size(); i++) {
            yardTokens.add(new YardToken(shipyard.getCards().get(i), getX(), getY()+ layout.height*(2+i*2)));
        }
    }

    public void switchVisibility() { visible = !visible; }

    public boolean isVisible() { return visible; }

    public void setVisible(boolean visibility) { this.visible = visible; }

    public Shipyard getShipyard() { return shipyard; }

    public ArrayList<YardToken> getYardTokens() { return yardTokens; }



}
