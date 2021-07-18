package com.darkgran.farstar.gui.tokens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.darkgran.farstar.gui.AssetLibrary;
import com.darkgran.farstar.gui.ColorPalette;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.gui.TextDrawer;
import com.darkgran.farstar.gui.TextLine;
import com.darkgran.farstar.util.SimpleVector2;

public class TokenPart extends TextLine {
    enum ContentState {
        NORMAL("padNormal", ColorPalette.BLACKISH), DAMAGED("padOutlined", ColorPalette.RED), UPGRADED("padOutlined", ColorPalette.GREEN);
        private final String fontName;
        private final Color color;
        ContentState(String fontName, Color color) {
            this.fontName = fontName;
            this.color = color;
        }
        public String getFontName() { return fontName; }
        public Color getColor() { return color; }
    }
    private final Token token;
    private Texture pad;
    private SimpleVector2 textWH;
    private float offsetY = 0f;
    private float offsetX = 0f;
    private ContentState currentContentState = ContentState.NORMAL;

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
            resetContentState();
            if (!TokenType.isDeployed(getToken().getTokenType())) {
                textWH = TextDrawer.getTextWH(getFont(), getContent());
            } else {
                textWH = TextDrawer.getTextWH(getContentStateFont(getCurrentContentState(), getToken().getTokenType()), getContent());
            }
            adjustTextWH();
            setPad(getToken().getTokenType());
        }
    }

    public void adjustTextWH() {
        if (getContent().equals("1")) {
            textWH.setX(textWH.getX()+2f);
        }
        adjustTextWHByCurrentState();
    }

    public void adjustTextWHByCurrentState() {
        if (getCurrentContentState() != ContentState.NORMAL) {
            switch (getToken().getTokenType()) {
                case MS:
                case FLEET:
                    getTextWH().setY(getTextWH().getY()*1.075f);
                    break;
                case PRINT:
                    getTextWH().setX(getTextWH().getX()*1.1f);
                    getTextWH().setY(getTextWH().getY()*1.15f);
                    break;
                case SUPPORT:
                    getTextWH().setY(getTextWH().getY()*1.1f);
                    break;
            }
        } else if (getToken().getTokenType() == TokenType.PRINT) {
            getTextWH().setY(getTextWH().getY()*0.98f);
        }
    }

    public void resetContentState() {}

    public void draw(Batch batch) {
        if (isEnabled()) {
            batch.draw(pad, getX() - pad.getWidth() + offsetX, getY() + offsetY);
            float textX = getX() - pad.getWidth()/2f - textWH.getX()/2f + offsetX;
            float textY = getY() + pad.getHeight()/2f + textWH.getY()/2f + offsetY;
            if (!(TokenType.isDeployed(getToken().getTokenType()) || getToken().getTokenType() == TokenType.PRINT)) {
                drawText(getFont(), batch, textX, textY, getContent(), ColorPalette.BLACKISH);
            } else {
                drawText(getContentStateFont(getCurrentContentState(), getToken().getTokenType()), batch, textX, textY, getContent(), getCurrentContentState().getColor());
            }
        }
    }

    public BitmapFont getContentStateFont(ContentState contentState, TokenType tokenType) {
        return Farstar.ASSET_LIBRARY.get(AssetLibrary.addTokenTypeAcronym("fonts/"+ contentState.getFontName() + "_", tokenType, true)+".fnt");
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

    public ContentState getCurrentContentState() {
        return currentContentState;
    }

    public void setCurrentContentState(ContentState currentContentState) {
        this.currentContentState = currentContentState;
    }
}
