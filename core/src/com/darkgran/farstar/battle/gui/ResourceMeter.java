package com.darkgran.farstar.battle.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.darkgran.farstar.ColorPalette;
import com.darkgran.farstar.battle.players.Player;
import com.darkgran.farstar.gui.JustFont;
import com.darkgran.farstar.util.SimpleVector2;
import com.darkgran.farstar.gui.TextDrawer;

public class ResourceMeter extends Actor implements JustFont {
    private final Player player;
    private final boolean onBottom;
    private String fontPath = "";

    public ResourceMeter(Player player, boolean onBottom, float x, float y) {
        setFont("fonts/bahnschrift40b.fnt");
        this.player = player;
        this.onBottom = onBottom;
        String res = "123456";
        SimpleVector2 textWH = TextDrawer.getTextWH(getFont(), res);
        setWidth(textWH.getX());
        setHeight(textWH.getY());
        setX(x);
        setY(y);
    }

    public void draw(Batch batch) {
        getFont().setColor(ColorPalette.MAIN);
        getFont().draw(batch, Integer.toString(player.getEnergy()), getX(), onBottom ? getY()+getHeight() : getY());
        getFont().setColor(ColorPalette.MAIN);
        getFont().draw(batch, Integer.toString(player.getMatter()), getX()+getWidth()/2, onBottom ? getY()+getHeight() : getY());
        getFont().setColor(Color.WHITE);
    }

    @Override
    public String getFontPath() {
        return fontPath;
    }

    @Override
    public void setFontPath(String path) {
        fontPath = path;
    }
}
