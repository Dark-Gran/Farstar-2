package com.darkgran.farstar.battle.gui.tokens;

import com.badlogic.gdx.graphics.Color;
import com.darkgran.farstar.gui.TextInTheBox;

public class Explainer extends TextInTheBox {
    public Explainer(Color fontColor, Color boxColor, String fontPath, String text) {
        super(fontColor, boxColor, fontPath, text);
    }

    public void setPosition(float x, float y) {
        setX(x);
        setY(y);
    }
}
