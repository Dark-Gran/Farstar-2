package com.darkgran.farstar.mainscreen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.util.TextDrawer;

public class VersionInfo extends Actor implements TextDrawer { //possibly: create TextDrawer+Actor parent implementation? ("TextWithVector" x "TextActor" x "TextInTheBox")
    private String fontPath = "";
    private Color fontColor = new Color();

    public VersionInfo(float x, float y, Color fontColor) {
        setFont("fonts/barlow24.fnt");
        setX(x);
        setY(y);
        setFontColor(fontColor);
    }

    @Override
    public void drawText(Batch batch) {
        drawText(getFont(), batch, getX(), getY(), Farstar.APP_VERSION, getFontColor());
    }

    @Override
    public String getFontPath() {
        return fontPath;
    }

    @Override
    public void setFontPath(String path) {
        fontPath = path;
    }

    @Override
    public Color getFontColor() {
        return fontColor;
    }

    @Override
    public void setFontColor(Color fontColor) {
        this.fontColor = fontColor;
    }
}
