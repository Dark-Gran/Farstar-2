package com.darkgran.farstar.battle.gui.tokens;

import com.badlogic.gdx.graphics.Color;
import com.darkgran.farstar.ColorPalette;
import com.darkgran.farstar.battle.players.cards.Card;
import com.darkgran.farstar.gui.TextInTheBox;

public class Explainer extends TextInTheBox {

    public Explainer(Color fontColor, Color boxColor, String fontPath, String text) {
        super(fontColor, boxColor, fontPath, text);
    }

    public Explainer() {
        super(ColorPalette.LIGHT, ColorPalette.changeAlpha(ColorPalette.DARK, 0.5f), "fonts/bahnschrift30.fnt", "");
        setWrapWidth(200f);
        setWrap(true);
    }

    public void setPosition(float x, float y) {
        setX(x+TokenType.PRINT.getWidth()*1.1f);
        setY(y+TokenType.PRINT.getHeight()*0.95f);
    }

    public void refreshText(Card card) {
        setText(getExplanation(card));
    }

    protected String getExplanation(Card card) {
        return "aaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
    }

}
