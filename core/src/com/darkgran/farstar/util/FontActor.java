package com.darkgran.farstar.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class FontActor extends Actor {
    private BitmapFont defaultFont = new BitmapFont();
    private BitmapFont barlow24 = new BitmapFont(Gdx.files.internal("fonts/barlow24.fnt"));

    public BitmapFont getFont() { return defaultFont; }

    public BitmapFont getBarlow24() {
        return barlow24;
    }

    public void dispose() {
        defaultFont.dispose();
        barlow24.dispose();
    }

}
