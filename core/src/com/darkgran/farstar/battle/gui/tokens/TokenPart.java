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
    private SimpleVector2 textWH;

    public TokenPart(String fontPath, Token token) {
        super(fontPath);
        this.token = token;
        if (!token.isNoPics()) {
            updateWH();
            setPad();
        }
    }

    public void setPad() {
        pad = Farstar.ASSET_LIBRARY.get(Farstar.ASSET_LIBRARY.addTokenTypeAcronym("images/portraits/padI", getToken().getTokenType())+".png");
    }

    public String getContent() {
        return "";
    }

    public void updateWH() {
        textWH = TextDrawer.getTextWH(getFont(), getContent());
    }

    public void draw(Batch batch) {
        batch.draw(pad, getX(), getY()-pad.getHeight());
        drawText(getFont(), batch, getX()+pad.getWidth()*0.5f-textWH.getX()*0.5f, getY()-pad.getHeight()*0.5f+textWH.getY()*0.5f, getContent());
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
