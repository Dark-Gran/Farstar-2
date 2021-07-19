package com.darkgran.farstar.gui.tokens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.battle.players.BattleCard;
import com.darkgran.farstar.gui.ColorPalette;
import com.darkgran.farstar.gui.battlegui.BattleStage;
import com.darkgran.farstar.gui.battlegui.CardListMenu;

import static com.darkgran.farstar.SuperScreen.DEBUG_RENDER;

public class DeploymentCard extends DeploymentToken implements CardGFX {
    private Color fontColor = ColorPalette.BLACKISH;
    private Texture cardPic;

    public DeploymentCard(BattleCard battleCard, float x, float y, BattleStage battleStage, CardListMenu cardListMenu) {
        super(battleCard, x, y, battleStage, cardListMenu);
        setWidth(TokenType.FAKE.getWidth());
        setHeight(361f);
        resetCardGFX(getCard().getCardInfo().getCulture(), TokenType.FAKE);
        setGlowOffsetX(-getGlowG().getWidth()/2f+getFrame().getWidth()/2f);
        setGlowOffsetY(-getGlowG().getHeight()/2f+getCardPic().getHeight()/2f);
        setOriginX(getWidth()/2);
        setOriginY(getHeight()/2);
        setParts();
    }

    @Override
    public void setGlows() {
        setGlowG(Farstar.ASSET_LIBRARY.get("images/tokens/glowG_U.png"));
        setGlowY(Farstar.ASSET_LIBRARY.get("images/tokens/glowY_U.png"));
    }

    @Override
    public void draw(Batch batch) {
        if (getCard() != null && !isNoPics()) {
            drawGlows(batch);
            drawCardGFX(batch, getX(), getY(), getTokenType());
            drawPortrait(batch);
            getTokenDefense().draw(batch);
            getTokenOffense().draw(batch);
            getTokenPrice().draw(batch);
        }
        if (DEBUG_RENDER) {
            debugRender(batch);
        }
    }

    @Override
    protected void drawPortrait(Batch batch) {
        if (getPortrait() != null) { batch.draw(getPortrait(), getX(), getY()+getCardPic().getHeight()-getPortrait().getHeight()+PORTRAIT_OFFSET_Y*2/3); }
        if (getFrame() != null) { batch.draw(getFrame(), getX(), getY()+getCardPic().getHeight()-getFrame().getHeight()+PORTRAIT_OFFSET_Y*2/3); }
    }

    @Override
    public void destroy() {
        super.destroy();
        if (getCard().getToken() instanceof HandToken) { ((HandToken) getCard().getToken()).setHidden(false); }
    }

    @Override
    public void setCardPic(Texture texture) {
        cardPic = texture;
    }

    @Override
    public Texture getCardPic() {
        return cardPic;
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
