package com.darkgran.farstar.battle.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.darkgran.farstar.ColorPalette;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.battle.players.Player;
import com.darkgran.farstar.gui.JustFont;
import com.darkgran.farstar.util.SimpleVector2;
import com.darkgran.farstar.gui.TextDrawer;

public class ResourceMeter extends Actor implements JustFont {
    private final Player player;
    private final boolean onBottom;
    private String fontPath = "";
    private Texture enePic = Farstar.ASSET_LIBRARY.getAssetManager().get("images/energy.png");
    private Texture matPic = Farstar.ASSET_LIBRARY.getAssetManager().get("images/matter.png");

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
        batch.draw(enePic, getX(), getY()-enePic.getHeight()*0.5f);
        getFont().draw(batch, Integer.toString(player.getEnergy()), getX()+enePic.getWidth(), onBottom ? getY()+getHeight() : getY());
        SimpleVector2 textWH = TextDrawer.getTextWH(getFont(), Integer.toString(player.getEnergy()));
        getFont().setColor(ColorPalette.MAIN);
        batch.draw(matPic, getX()+enePic.getWidth()+textWH.getX(), getY()-matPic.getHeight()*0.5f);
        getFont().draw(batch, Integer.toString(player.getMatter()), getX()+enePic.getWidth()+ textWH.getX()+matPic.getWidth(), onBottom ? getY()+getHeight() : getY());
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
