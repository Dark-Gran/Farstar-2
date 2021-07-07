package com.darkgran.farstar.battle.gui.tokens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.battle.gui.BattleStage;
import com.darkgran.farstar.battle.gui.CardListMenu;
import com.darkgran.farstar.battle.players.cards.Card;
import com.darkgran.farstar.battle.players.cards.CardType;
import com.darkgran.farstar.util.SimpleVector2;

public class HandToken extends AnchoredToken {
    public enum HandState {
        DOWN, UP;
    }
    private static final float PORTRAIT_OFFSET_Y = -16f;
    private Texture cardPic;
    private HandState currentState = HandState.DOWN;
    private HandState nextState = currentState;
    private float angle = 0f;
    Matrix4 oldMX = null;
    Matrix4 mx;
    public static SimpleVector2 getNewXYFromAngle(float currentX, float currentY, float centerX, float centerY, double rads) {
        return new SimpleVector2(
                (float) ((currentX-centerX) * Math.cos(rads) - (currentY-centerY) * Math.sin(rads) + centerX),
                (float) ((currentX-centerX) * Math.sin(rads) + (currentY-centerY) * Math.cos(rads) + centerY)
        );
    }


    public HandToken(Card card, float x, float y, BattleStage battleStage, CardListMenu cardListMenu) {
        super(card, x, y, battleStage, cardListMenu, TokenType.HAND, false);
        setDragger(new ManagedDragger(this, battleStage.getBattleScreen().getBattle().getRoundManager(), false){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                nextState = button == 0 ? HandState.UP : HandState.DOWN;
                if (isEnabled() && button == 0 && getCard().getCardInfo().getCardType() == CardType.BLUEPRINT) {
                    getCardListMenu().getPlayer().getFleet().getFleetMenu().setPredictEnabled(true);
                }
                refreshSize();
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                nextState = HandState.DOWN;
                refreshSize();
                super.touchUp(event, x, y, pointer, button);
            }
        });
        this.addListener(getDragger());
        cardPic = Farstar.ASSET_LIBRARY.get("images/tokens/card_D.png");
        setOriginX(getWidth()/2);
        setOriginY(getHeight()/2);
        refreshRotation();
    }

    public void refreshSize() {
        if (currentState != nextState) {
            TokenType tokenType = TokenType.HAND;
            if (nextState == HandState.UP) {
                tokenType = TokenType.FAKE;
            }
            setWidth(tokenType.getWidth());
            setHeight(tokenType == TokenType.HAND ? tokenType.getHeight() : 361f);
            setFont(tokenType.getFontPath());
            if (!isNoPics()) {
                setPortrait(Farstar.ASSET_LIBRARY.get(Farstar.ASSET_LIBRARY.getPortraitName(getCard().getCardInfo(), tokenType)));
                setFrame(Farstar.ASSET_LIBRARY.get(Farstar.ASSET_LIBRARY.getFrameName(getCard().getCardInfo(), tokenType)));
                cardPic = Farstar.ASSET_LIBRARY.get("images/tokens/card_"+(nextState == HandState.UP ? "U" : "D")+".png");
            }
            getTokenDefense().setPad(tokenType);
            getTokenOffense().setPad(tokenType);
            getTokenPrice().setPad(tokenType);
            setPosition(getX(), getY());
            setOriginX(getWidth()/2);
            setOriginY(getHeight()/2);
            currentState = nextState;
        }
    }

    public void refreshRotation() {
        angle = getNewAngle();
        mx = new Matrix4();
        mx.rotate(new Vector3(0, 0, 1), angle);
        SimpleVector2 newXY = getNewXYFromAngle(0, 0, getX()+getWidth()/2, getY()+getHeight()/2, Math.toRadians(angle));
        mx.trn(newXY.getX(), newXY.getY(), 0);
        setRotation(angle);
    }

    private float getNewAngle() {
        float degrees = 45f;
        //todo
        return degrees;
    }

    @Override
    public void draw(Batch batch) {
        //Rotate
        if (currentState == HandState.DOWN) {
            oldMX = batch.getTransformMatrix().cpy();
            batch.setTransformMatrix(mx);
        } else if (oldMX != null) {
            oldMX = null;
        }
        //Draw
        batch.draw(cardPic, getX(), getY());
        super.draw(batch);
        if (oldMX != null) { batch.setTransformMatrix(oldMX); }
    }

    @Override
    protected void drawPortrait(Batch batch) {
        if (getPortrait() != null) { batch.draw(getPortrait(), getX(), getY()+cardPic.getHeight()-getPortrait().getHeight()+PORTRAIT_OFFSET_Y); }
        if (getFrame() != null) { batch.draw(getFrame(), getX(), getY()+cardPic.getHeight()-getFrame().getHeight()+PORTRAIT_OFFSET_Y); }
    }

    @Override
    protected void debugRender(Batch batch) {
        if (currentState == HandState.DOWN) {
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

}
