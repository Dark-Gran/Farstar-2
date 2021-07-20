package com.darkgran.farstar.gui.tokens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.darkgran.farstar.battle.BattleType;
import com.darkgran.farstar.battle.players.LocalBattlePlayer;
import com.darkgran.farstar.gui.ColorPalette;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.gui.battlegui.BattleStage;
import com.darkgran.farstar.gui.battlegui.CardListMenu;
import com.darkgran.farstar.battle.players.BattleCard;
import com.darkgran.farstar.util.SimpleVector2;

import static com.darkgran.farstar.SuperScreen.DEBUG_RENDER;

/**
 * Used for "card-zoom" etc.
 */
public class PrintToken extends Token implements CardGFX {
    private Color fontColor = ColorPalette.BLACKISH;
    private Texture cardPic;
    private TokenType targetType;
    private SimpleVector2 targetXY = new SimpleVector2(0, 0);

    public PrintToken(BattleCard battleCard, float x, float y, BattleStage battleStage, CardListMenu cardListMenu, boolean connectCard) {
        super(battleCard, x, y, battleStage, cardListMenu, TokenType.PRINT, false, connectCard);
        setFont("fonts/bahnschrift30.fnt");
        setTouchable(Touchable.disabled);
    }

    public void enable(BattleCard battleCard, TokenType targetType, SimpleVector2 targetXY) {
        if (getCard() != battleCard) {
            setup(battleCard, targetType, targetXY);
            if (getGlowG() == null) { setGlows(); }
        }
    }

    @Override
    public void setGlows() {
        setGlowG(Farstar.ASSET_LIBRARY.get("images/tokens/glowG_Z.png"));
        setGlowY(Farstar.ASSET_LIBRARY.get("images/tokens/glowY_Z.png"));
        setGlowOffsetX(-getGlowG().getWidth()/2f+getFrame().getWidth()/2f);
        setGlowOffsetY(-getGlowG().getHeight()/2f+getCardPic().getHeight()/2f);
    }

    @Override
    public void setup(BattleCard battleCard, TokenType targetType, SimpleVector2 targetXY) {
        super.setup(battleCard, targetType, targetXY);
        resetCardGFX(battleCard.getCardInfo().getCulture(), TokenType.PRINT);
        setGlowState(battleCard.getToken().getGlowState());
        this.targetType = targetType;
        this.targetXY = targetXY;
        shiftPosition();
    }

    public void shiftPosition() { }

    public void disable() {
        setCard(null);
    }

    @Override
    public void draw(Batch batch) {
        if (getCard() != null) {
            drawGlows(batch);
            drawCardGFX(batch, getX(), getY(), getTokenType());
            drawPortrait(batch);
            getTokenDefense().draw(batch);
            getTokenOffense().draw(batch);
            getTokenPrice().draw(batch);
            if (DEBUG_RENDER) {
                debugRender(batch);
            }
        }
    }

    @Override
    protected void drawPortrait(Batch batch) {
        if (getPortrait() != null) { batch.draw(getPortrait(), getX(), getY()+getCardPic().getHeight()-getPortrait().getHeight()+PORTRAIT_OFFSET_Y); }
        if (getFrame() != null) { batch.draw(getFrame(), getX(), getY()+getCardPic().getHeight()-getFrame().getHeight()+PORTRAIT_OFFSET_Y); }
    }

    public TokenType getTargetType() {
        return targetType;
    }

    public SimpleVector2 getTargetXY() {
        return targetXY;
    }

    @Override
    public void setCardPic(Texture texture) {
        cardPic = texture;
    }

    public Texture getCardPic() {
        return cardPic;
    }

    @Override
    public boolean isBackside() {
        return false;
    }

    @Override
    public Color getFontColor() {
        return fontColor;
    }

    @Override
    public void setFontColor(Color fontColor) {
        this.fontColor = fontColor;
    }
}
