package com.darkgran.farstar.gui.tokens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.darkgran.farstar.gui.AssetLibrary;
import com.darkgran.farstar.gui.ColorPalette;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.gui.TextDrawer;
import com.darkgran.farstar.gui.TextLine;
import com.darkgran.farstar.gui.SimpleVector2;

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
    private TextureRegion pad;
    private SimpleVector2 textWH;
    private float offsetY = 0f;
    private float offsetX = 0f;
    private float textOffsetX = 0f;
    private float textOffsetY = 0f;
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
        if (TokenType.isDeployed(token.getTokenType())) { //quick-fix for position rounding; in-future: check Fleets even/odd positions + look into how the text is actually placed/centered
            if (getContent().equals("1") || getContent().equals("0") || getContent().length() > 1) {
                setTextOffsetX(1f);
            }
        }
    }

    public void setPad(TokenType tokenType) {
        pad = Farstar.ASSET_LIBRARY.getAtlasRegion(AssetLibrary.addTokenTypeAcronym("padI-", tokenType, true));
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
            textWH.x = (textWH.x+2f);
        }
        adjustTextWHByCurrentState();
    }

    public void adjustTextWHByCurrentState() {
        if (getCurrentContentState() != ContentState.NORMAL) {
            switch (getToken().getTokenType()) {
                case MS:
                case FLEET:
                    getTextWH().y = getTextWH().y*1.075f;
                    break;
                case PRINT:
                    getTextWH().x = getTextWH().x*1.1f;
                    getTextWH().y = getTextWH().y*1.15f;
                    break;
                case SUPPORT:
                    getTextWH().y = getTextWH().y*1.1f;
                    break;
            }
        } else if (getToken().getTokenType() == TokenType.PRINT) {
            getTextWH().y = getTextWH().y*0.98f;
        }
    }

    public void resetContentState() {}

    public void draw(Batch batch) {
        token.getBattleStage().getShotManager().drawRecoil(batch, token);
        if (isEnabled()) {
            batch.draw(pad, x - pad.getRegionWidth() + offsetX, y + offsetY);
            float textX = x - pad.getRegionWidth()/2f - textWH.x/2f + offsetX + textOffsetX;
            float textY = y + pad.getRegionHeight()/2f + textWH.y/2f + offsetY + textOffsetY;
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

    public TextureRegion getPad() {
        return pad;
    }

    public void setPad(TextureRegion pad) {
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

    public float getTextOffsetY() {
        return textOffsetY;
    }

    public void setTextOffsetY(float textOffsetY) {
        this.textOffsetY = textOffsetY;
    }

    public float getTextOffsetX() {
        return textOffsetX;
    }

    public void setTextOffsetX(float textOffsetX) {
        this.textOffsetX = textOffsetX;
    }
}
