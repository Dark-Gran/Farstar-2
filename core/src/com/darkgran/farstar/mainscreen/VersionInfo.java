package com.darkgran.farstar.mainscreen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.util.JustFont;
import com.darkgran.farstar.util.TextDrawer;

public class VersionInfo extends Actor implements TextDrawer, JustFont {

    public VersionInfo(float x, float y, Color fontColor) {
        setFont("fonts/barlow24.fnt");
        setX(x);
        setY(y);
        setFontColor(fontColor);
    }

    @Override
    public void draw(Batch batch) {
        draw(getFont(), batch, getX(), getY(), Farstar.APP_VERSION, fontColor);
    }

}
