package com.darkgran.farstar.mainscreen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.darkgran.farstar.util.FontActor;
import com.darkgran.farstar.util.TextDrawer;

public class VersionInfo extends FontActor implements TextDrawer {
    private Color fontColor;

    public VersionInfo(float x, float y, Color fontColor) {
        setX(x);
        setY(y);
        this.fontColor = fontColor;
    }

    public void draw(Batch batch) {
        String txt = "v.0.1 (Alpha)";
        draw(getFont(), batch, getX(), getY(), txt, fontColor);
    }

    public Color getFontColor() {
        return fontColor;
    }

    public void setFontColor(Color fontColor) {
        this.fontColor = fontColor;
    }
}
