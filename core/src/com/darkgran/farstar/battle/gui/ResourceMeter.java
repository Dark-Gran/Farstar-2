package com.darkgran.farstar.battle.gui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.darkgran.farstar.battle.players.Player;

public class ResourceMeter extends TextFont {
    private final Player player;
    private final boolean onBottom;

    public ResourceMeter(Player player, boolean onBottom, float x, float y) {
        this.player = player;
        this.onBottom = onBottom;
        String res = "Population: 999";
        GlyphLayout layout = new GlyphLayout();
        layout.setText(new BitmapFont(), res);
        setWidth(layout.width);
        setHeight(layout.height);
        setX(x);
        setY(y);
    }

    public void draw(Batch batch) {
        String res = "Energy: "+player.getEnergy();
        getFont().draw(batch, res, getX(), onBottom ? getY()+getHeight()*8 : getY()-getHeight()*6);
        res = " Matter: "+player.getMatter();
        getFont().draw(batch, res, getX(), onBottom ? getY()+getHeight()*6 : getY()-getHeight()*8);
    }

}
