package com.darkgran.farstar.battle.gui.tokens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.darkgran.farstar.AssetLibrary;
import com.darkgran.farstar.ColorPalette;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.gui.TextDrawer;
import com.darkgran.farstar.gui.TextLine;
import com.darkgran.farstar.util.SimpleVector2;

public class TokenPart extends TextLine {
    enum ContentState {
        NORMAL("black"), DAMAGED("red"), UPGRADED("green");
        private final String font;
        ContentState(String font) { this.font = font; }
        public String getFontName() { return font; }
    }
    private final Token token;
    private Texture pad;
    private SimpleVector2 textWH;
    private float offsetY = 0f;
    private float offsetX = 0f;
    private ContentState contentState = ContentState.NORMAL;

    public TokenPart(String fontPath, Token token) {
        super(fontPath);
        this.token = token;
        if (!token.isNoPics()) {
            if (token.getCard() != null) {
                update();
                setPad(token.getTokenType());
                setupOffset();
            }
        }
    }

    public void setupOffset() {
        setOffsetY(0f);
    }

    public void setPad(TokenType tokenType) {
        pad = Farstar.ASSET_LIBRARY.get(AssetLibrary.addTokenTypeAcronym("images/tokens/padI_", tokenType, true)+".png");
    }

    public String getContent() {
        return "";
    }

    public void update() {
        if (getToken().getCard() != null) {
            textWH = TextDrawer.getTextWH(getFont(), getContent());
            adjustTextWH();
            setPad(getToken().getTokenType());
            resetContentState();
        }
    }

    public void adjustTextWH() {
        if (getContent().equals("1")) {
            textWH.setX(textWH.getX()+2f);
        }
    }

    public void resetContentState() {}

    public void draw(Batch batch) {
        if (isEnabled()) {
            batch.draw(pad, getX() - pad.getWidth() + offsetX, getY() + offsetY);
            if (!TokenType.isDeployed(getToken().getTokenType())) {
                drawText(getFont(), batch, getX() - pad.getWidth() * 0.5f - textWH.getX() * 0.5f + offsetX, getY() + offsetY + pad.getHeight() * 0.5f + textWH.getY() * 0.5f, getContent(), ColorPalette.BLACKISH);
            } else {
                drawText(Farstar.ASSET_LIBRARY.getFont(getToken().getTokenType().getDefaultFontSize(), getContentState().getFontName()), batch, getX() - pad.getWidth() * 0.5f - textWH.getX() * 0.5f + offsetX, getY() + offsetY + pad.getHeight() * 0.5f + textWH.getY() * 0.5f, getContent());
            }
        }
    }

    public boolean isEnabled() {
        return true;
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

    public ContentState getContentState() {
        return contentState;
    }

    public void setContentState(ContentState contentState) {
        this.contentState = contentState;
    }
}
