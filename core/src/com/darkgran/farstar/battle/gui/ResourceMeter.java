package com.darkgran.farstar.battle.gui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.darkgran.farstar.battle.Player;

public class ResourceMeter {
    private BitmapFont font = new BitmapFont();
    private final Player player;
    private final boolean onBottom;
    private final float x;
    private final float y;
    private final float width;
    private final float height;

    public ResourceMeter(Player player, boolean onBottom, float x, float y) {
        this.player = player;
        this.onBottom = onBottom;
        String res = "Population: 999";
        GlyphLayout layout = new GlyphLayout();
        layout.setText(new BitmapFont(), res);
        width = layout.width;
        height = layout.height;
        this.x = x-width*2;
        this.y = y;
    }

    public void draw(Batch batch) {
        String res = "Energy: "+player.getEnergy();
        font.draw(batch, res, x, onBottom ? y+height*8 : y-height*6);
        res = " Matter: "+player.getMatter();
        font.draw(batch, res, x, onBottom ? y+height*6 : y-height*8);
    }

}
