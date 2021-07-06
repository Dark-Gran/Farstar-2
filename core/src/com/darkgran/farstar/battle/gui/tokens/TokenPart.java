package com.darkgran.farstar.battle.gui.tokens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.gui.TextLine;

public class TokenPart extends TextLine {
    private final Token token;
    private Texture pad;

    public TokenPart(String fontPath, Token token) {
        super(fontPath);
        this.token = token;
        setPad();
    }

    public void setPad() {
        pad = Farstar.ASSET_LIBRARY.getAssetManager().get("images/portraits/pad_I.png");
    }

    public String getContent() {
        return "";
    }

    public void draw(Batch batch) {
        batch.draw(pad, getX(), getY()-pad.getHeight());;
        drawText(getFont(), batch, getX(), getY(), getContent());
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
