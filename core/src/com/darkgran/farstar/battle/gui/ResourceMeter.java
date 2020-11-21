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

    public ResourceMeter(Player player, boolean onBottom, float x, float y) {
        this.player = player;
        this.onBottom = onBottom;
        this.x = x;
        this.y = y;
    }

    public void draw(Batch batch) {
        String res = "Energy: "+player.getEnergy();
        GlyphLayout layout = new GlyphLayout();
        layout.setText(new BitmapFont(), res);
        font.draw(batch, res, x-layout.width*3, onBottom ? y+layout.height*6 : y-layout.height*6);
    }

}
