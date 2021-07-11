package com.darkgran.farstar.battle.gui.tokens;

import com.badlogic.gdx.graphics.Color;
import com.darkgran.farstar.gui.TextInTheBox;

public class Explainer extends TextInTheBox {
    public Explainer(Color fontColor, Color boxColor, String fontPath, String text, boolean wrap, float wrapWidth) {
        super(fontColor, boxColor, fontPath, text);
        setWrapWidth(wrapWidth);
        setWrap(wrap);
    }

    public void setPosition(float x, float y) {
        setX(x+TokenType.PRINT.getWidth()*1.1f);
        setY(y+TokenType.PRINT.getHeight()*0.95f);
    }
}
