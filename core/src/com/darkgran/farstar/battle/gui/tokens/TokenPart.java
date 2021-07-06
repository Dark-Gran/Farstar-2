package com.darkgran.farstar.battle.gui.tokens;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.darkgran.farstar.gui.TextLine;

public class TokenPart extends TextLine {
    private final Token token;

    public TokenPart(String fontPath, Token token) {
        super(fontPath);
        this.token = token;
    }

    public void draw(Batch batch) {
        drawText(getFont(), batch, getX(), getY(), Integer.toString(token.getCard().getHealth()));
    }

}
