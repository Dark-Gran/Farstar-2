package com.darkgran.farstar.battle.gui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.darkgran.farstar.util.SimpleBox2;

public abstract class TextFont extends SimpleBox2 {
    private BitmapFont font = new BitmapFont();

    public BitmapFont getFont() { return font; }



}
