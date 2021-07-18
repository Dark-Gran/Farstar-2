package com.darkgran.farstar.gui.tokens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.darkgran.farstar.battle.players.BattleCard;
import com.darkgran.farstar.gui.AssetLibrary;
import com.darkgran.farstar.gui.ColorPalette;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.gui.battlegui.BattleStage;
import com.darkgran.farstar.gui.battlegui.CardListMenu;
import com.darkgran.farstar.gui.battlegui.HandMenu;
import com.darkgran.farstar.cards.CardCulture;
import com.darkgran.farstar.cards.CardType;
import com.darkgran.farstar.util.SimpleVector2;

import static com.darkgran.farstar.SuperScreen.DEBUG_RENDER;

public class HandToken extends AnchoredToken implements CardGFX {
    public enum HandTokenState {

        DOWN("D", TokenType.HAND),
        UP("U", TokenType.FAKE);

        private final String acronym;
        private final TokenType tokenType;
        HandTokenState(String acronym, TokenType tokenType) {
            this.tokenType = tokenType;
            this.acronym = acronym;
        }
        public String getAcronym() { return acronym; }
        public TokenType getTokenType() { return tokenType; }

    }
    private Color fontColor = ColorPalette.BLACKISH;
    private Texture cardPic;
    private HandTokenState currentState = HandTokenState.DOWN;
    private HandTokenState nextState = currentState;
    Matrix4 oldMX = null;
    Matrix4 mx;
    public static SimpleVector2 getNewXYFromAngle(float currentX, float currentY, float centerX, float centerY, double rads) {
        return new SimpleVector2(
                (float) ((currentX-centerX) * Math.cos(rads) - (currentY-centerY) * Math.sin(rads) + centerX),
                (float) ((currentX-centerX) * Math.sin(rads) + (currentY-centerY) * Math.cos(rads) + centerY)
        );
    }


    public HandToken(BattleCard battleCard, float x, float y, BattleStage battleStage, CardListMenu cardListMenu) {
        super(battleCard, x, y, battleStage, cardListMenu, TokenType.HAND, false, true);
        setDragger(new ManagedDragger(this, battleStage.getBattleScreen().getBattle().getRoundManager(), false){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                nextState = button == 0 ? HandTokenState.UP : HandTokenState.DOWN;
                if (isEnabled() && button == 0 && getCard().getCardInfo().getCardType() == CardType.BLUEPRINT) {
                    getCardListMenu().getPlayer().getFleet().getFleetMenu().setPredictEnabled(true);
                }
                refreshSize();
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                nextState = HandTokenState.DOWN;
                refreshSize();
                super.touchUp(event, x, y, pointer, button);
            }
        });
        this.addListener(getDragger());
        resetCardGFX(getCard().getCardInfo().getCulture(), null);
        setGlowOffsetX(-getGlowG().getWidth()/2f+getFrame().getWidth()/2f);
        setGlowOffsetY(-getGlowG().getHeight()/2f+getCardPic().getHeight()/2f);
        setOriginX(getWidth()/2);
        setOriginY(getHeight()/2);
    }

    @Override
    public void resetPosition() {
        float offsetY = getCardListMenu().isNegativeOffset() ? -285f : 250f;
        setPosition(getAnchorX(), getAnchorY() + (((HandMenu)getCardListMenu()).getHandState() == HandMenu.HandMenuState.UP ? offsetY : 0f));
    }

    @Override
    public void resetCardGFX(CardCulture culture, TokenType tokenType) {
        setCardPic(Farstar.ASSET_LIBRARY.get("images/tokens/card" + culture.getAcronym() + "_" + currentState.getAcronym() + ".png"));
    }

    @Override
    public void setGlows() {
        if (getCurrentState() != null) {
            setGlowG(Farstar.ASSET_LIBRARY.get("images/tokens/glowG_" + currentState.getAcronym() + ".png"));
            setGlowY(Farstar.ASSET_LIBRARY.get("images/tokens/glowY_" + currentState.getAcronym() + ".png"));
        } else {
            setGlowG(Farstar.ASSET_LIBRARY.get("images/tokens/glowG_D.png"));
            setGlowY(Farstar.ASSET_LIBRARY.get("images/tokens/glowY_D.png"));
        }
    }

    public void refreshSize() {
        if (currentState != nextState) {
            TokenType tokenType = TokenType.HAND;
            if (nextState == HandTokenState.UP) {
                tokenType = TokenType.FAKE;
            }
            currentState = nextState;
            setWidth(tokenType.getWidth());
            setHeight(tokenType == TokenType.HAND ? tokenType.getHeight() : 361f);
            setFont(AssetLibrary.getFontPath(tokenType.getDefaultFontSize(), "bahnschrift"));
            if (!isNoPics() && getCard() != null) {
                setPortrait(Farstar.ASSET_LIBRARY.get(Farstar.ASSET_LIBRARY.getPortraitName(getCard().getCardInfo(), tokenType)));
                setFrame(Farstar.ASSET_LIBRARY.get(Farstar.ASSET_LIBRARY.getFrameName(getCard().getCardInfo(), tokenType)));
                resetCardGFX(getCard().getCardInfo().getCulture(), tokenType);
            }
            getTokenDefense().setPad(tokenType);
            getTokenOffense().setPad(tokenType);
            getTokenPrice().setPad(tokenType);
            setPosition(getX(), getY());
            setOriginX(getWidth()/2);
            setOriginY(getHeight()/2);
        }
    }

    public void refreshRotation(int position, int battleCards) {
        float angle = getNewAngle(position, battleCards);
        mx = new Matrix4();
        mx.rotate(new Vector3(0, 0, 1), angle);
        SimpleVector2 newXY = getNewXYFromAngle(0, 0, getX()+getWidth()/2, getY()+getHeight()/2, Math.toRadians(angle));
        mx.trn(newXY.getX(), newXY.getY(), 0);
        setRotation(angle);
    }

    private float getNewAngle(int position, int battleCards) {
        final float step = 2f;
        final boolean even = battleCards % 2 == 0;
        float degrees = 0f;
        if (battleCards > 1) {
            int mid = Math.round(battleCards/2f)-1;
            if (even) {
                if (position <= mid) {
                    mid += 1;
                    degrees = ((mid-position)*step) - step/2;
                } else {
                    degrees = ((position-mid)*-step) + step/2;
                }
            } else {
                if (position < mid) {
                    degrees = (mid-position)*step;
                } else if (position > mid) {
                    degrees = (position-mid)*-step;
                }
            }
        }
        if (getCardListMenu().isNegativeOffset()) { degrees *= -1f; }
        return degrees;
    }

    @Override
    public void draw(Batch batch) {
        if (!(getClickListener().isOver() && getClickListener().getPressedButton() != -1 && currentState == HandTokenState.DOWN)) {
            //Rotate
            if (currentState == HandTokenState.DOWN) {
                oldMX = batch.getTransformMatrix().cpy();
                batch.setTransformMatrix(mx);
            } else if (oldMX != null) {
                oldMX = null;
            }
            //Draw
            if (getCard() != null) {
                drawGlows(batch);
                drawCardGFX(batch, getX(), getY(), currentState.getTokenType());
                drawPortrait(batch);
                getTokenDefense().draw(batch);
                getTokenOffense().draw(batch);
                getTokenPrice().draw(batch);
            }
            if (DEBUG_RENDER) {
                debugRender(batch);
            }
            if (oldMX != null) {
                batch.setTransformMatrix(oldMX);
            }
        }
    }

    @Override
    protected void drawPortrait(Batch batch) {
        if (getPortrait() != null) { batch.draw(getPortrait(), getX(), getY()+getCardPic().getHeight()-getPortrait().getHeight()+PORTRAIT_OFFSET_Y*2/3); }
        if (getFrame() != null) { batch.draw(getFrame(), getX(), getY()+getCardPic().getHeight()-getFrame().getHeight()+PORTRAIT_OFFSET_Y*2/3); }
    }

    @Override
    protected void debugRender(Batch batch) {
        if (currentState == HandTokenState.DOWN) {
            batch.end();
            getBattleStage().getBattleScreen().getShapeRenderer().begin(ShapeRenderer.ShapeType.Line);
            getBattleStage().getBattleScreen().getShapeRenderer().rect(getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), 1f, 1f, getRotation());
            getBattleStage().getBattleScreen().getShapeRenderer().end();
            batch.begin();
        } else {
            super.debugRender(batch);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        getCardListMenu().getPlayer().getFleet().getFleetMenu().setPredictEnabled(false);
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

    public HandTokenState getCurrentState() {
        return currentState;
    }
}
