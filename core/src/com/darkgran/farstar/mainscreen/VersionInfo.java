package com.darkgran.farstar.mainscreen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.darkgran.farstar.util.FontActor;
import com.darkgran.farstar.util.TextDrawer;

public class VersionInfo extends FontActor implements TextDrawer {

    public VersionInfo(float x, float y, Color fontColor) {
        super("fonts/barlow24.fnt");
        setX(x);
        setY(y);
        setFontColor(fontColor);
    }

    public void draw(Batch batch) {
        String txt = "v0.2 (Alpha)";
        draw(getFont(), batch, getX(), getY(), txt, fontColor);
    }

}
