package com.darkgran.farstar.battle.gui.tokens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.battle.players.cards.Card;
import com.darkgran.farstar.gui.TextDrawer;
import com.darkgran.farstar.gui.TextLine;
import com.darkgran.farstar.util.SimpleVector2;

public class TokenPart extends TextLine {
    private final Card card;
    private TokenType tokenType;
    private boolean noPics;
    private Texture pad;
    private SimpleVector2 textWH;
    private float offsetY = 0f;
    private float offsetX = 0f;

    public TokenPart(String fontPath, Card card, TokenType tokenType, boolean noPics) {
        super(fontPath);
        this.card = card;
        this.noPics = noPics;
        this.tokenType = tokenType;
        if (!noPics) {
            update();
            setPad(tokenType);
            setupOffset();
        }
    }

    public void setupOffset() {
        setOffsetY(1f);
    }

    public void setPad(TokenType tokenType) {
        pad = Farstar.ASSET_LIBRARY.get(Farstar.ASSET_LIBRARY.addTokenTypeAcronym("images/tokens/padI", tokenType, true)+".png");
    }

    public String getContent() {
        return "";
    }

    public void update() {
        textWH = TextDrawer.getTextWH(getFont(), getContent());
        adjustTextWH();
    }

    public void adjustTextWH() {
        if (getContent().equals("1")) {
            textWH.setX(textWH.getX()+2f);
        }
    }

    public void draw(Batch batch) {
        if (isEnabled()) {
            batch.draw(pad, getX() - pad.getWidth() + offsetX, getY() + offsetY);
            drawText(getFont(), batch, getX() - pad.getWidth() * 0.5f - textWH.getX() * 0.5f + offsetX, getY() + offsetY + pad.getHeight() * 0.5f + textWH.getY() * 0.48f, getContent());
        }
    }

    public boolean isEnabled() {
        return true;
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

    public float getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(float offsetX) {
        this.offsetX = offsetX;
    }

    public SimpleVector2 getTextWH() {
        return textWH;
    }

    public void setTextWH(SimpleVector2 textWH) {
        this.textWH = textWH;
    }

    public Card getCard() {
        return card;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    public boolean isNoPics() {
        return noPics;
    }

    public void setNoPics(boolean noPics) {
        this.noPics = noPics;
    }
}
