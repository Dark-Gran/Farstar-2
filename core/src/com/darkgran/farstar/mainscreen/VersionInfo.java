package com.darkgran.farstar.mainscreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.darkgran.farstar.util.FontActor;
import com.darkgran.farstar.util.TextDrawer;

public class VersionInfo extends FontActor implements TextDrawer {
    private Color fontColor;

    public VersionInfo(float x, float y, Color fontColor) {
        setX(x);
        setY(y);
        this.fontColor = fontColor;
        setFont(new BitmapFont(Gdx.files.internal("fonts/barlow24.fnt")));
    }

    public void draw(Batch batch) {
        String txt = "v0.2 (Alpha)";
        draw(getFont(), batch, getX(), getY(), txt, fontColor);
    }

    public Color getFontColor() {
        return fontColor;
    }

    public void setFontColor(Color fontColor) {
        this.fontColor = fontColor;
    }
}
