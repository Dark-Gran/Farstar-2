package com.darkgran.farstar.battle.gui.tokens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.gui.TextDrawer;
import com.darkgran.farstar.gui.TextLine;
import com.darkgran.farstar.util.SimpleVector2;

public class TokenPart extends TextLine {
    private final Token token;
    private Texture pad;
    private final SimpleVector2 textWH1;
    private final SimpleVector2 textWH2;

    public TokenPart(String fontPath, Token token) {
        super(fontPath);
        this.token = token;
        textWH1 = TextDrawer.getTextWH(getFont(), "0");
        textWH2 = TextDrawer.getTextWH(getFont(), "00");
        setPad();
    }

    public void setPad() {
        pad = Farstar.ASSET_LIBRARY.get(Farstar.ASSET_LIBRARY.addTokenTypeAcronym("images/portraits/padI", getToken().getTokenType())+".png");
    }

    public String getContent() {
        return "";
    }

    public void draw(Batch batch) {
        batch.draw(pad, getX(), getY()-pad.getHeight());
        drawText(getFont(), batch, getX()+pad.getWidth()*0.5f-(getContent().length() > 1 ? textWH2.getX() : textWH1.getX())*0.5f, getY()-pad.getHeight()*0.5f+(getContent().length() > 1 ? textWH2.getY() : textWH1.getY())*0.5f, getContent());
    }

    public Token getToken() {
        return token;
    }

    public Texture getPad() {
        return pad;
    }

    public void setPad(Texture pad) {
        this.pad = pad;
    }
}
