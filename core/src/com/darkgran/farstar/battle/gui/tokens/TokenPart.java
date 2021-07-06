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
    private float offsetY = 0f;

    public TokenPart(String fontPath, Token token) {
        super(fontPath);
        this.token = token;
        if (!token.isNoPics()) {
            update();
            setPad();
            setupOffset();
        }
    }

    public void setupOffset() { }

    public void setPad() {
        pad = Farstar.ASSET_LIBRARY.get(Farstar.ASSET_LIBRARY.addTokenTypeAcronym("images/portraits/padI", getToken().getTokenType())+".png");
    }

    public String getContent() {
        return "";
    }

    public void update() {
        textWH = TextDrawer.getTextWH(getFont(), getContent());
    }

    public void draw(Batch batch) {
        batch.draw(pad, getX()-pad.getWidth(), getY()+ offsetY);
        drawText(getFont(), batch, getX()-pad.getWidth()*0.5f-textWH.getX()*0.5f, getY()+1f+ offsetY +pad.getHeight()*0.5f+textWH.getY()*0.48f, getContent());
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

    public float getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(float offsetY) {
        this.offsetY = offsetY;
    }
}
