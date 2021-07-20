package com.darkgran.farstar.gui.tokens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.darkgran.farstar.battle.BattleType;
import com.darkgran.farstar.battle.players.BattleCard;
import com.darkgran.farstar.battle.players.LocalBattlePlayer;
import com.darkgran.farstar.gui.ColorPalette;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.gui.battlegui.BattleStage;
import com.darkgran.farstar.gui.battlegui.CardListMenu;
import com.darkgran.farstar.util.SimpleVector2;

import static com.darkgran.farstar.SuperScreen.DEBUG_RENDER;

public class HandToken extends AnchoredToken implements CardGFX, FakingTokens { //possibly in-future: use a slightly smaller version for the top side of the table (to simulate some visual perspective), but only if it's not a LocalPlayer (ie. outside Simulation)
    private Color fontColor = ColorPalette.BLACKISH;
    private Texture cardPic;
    Matrix4 oldMX = null;
    Matrix4 mx;
    private boolean hidden;
    public static SimpleVector2 getNewXYFromAngle(float currentX, float currentY, float centerX, float centerY, double rads) {
        return new SimpleVector2(
                (float) ((currentX-centerX) * Math.cos(rads) - (currentY-centerY) * Math.sin(rads) + centerX),
                (float) ((currentX-centerX) * Math.sin(rads) + (currentY-centerY) * Math.cos(rads) + centerY)
        );
    }


    public HandToken(BattleCard battleCard, float x, float y, BattleStage battleStage, CardListMenu cardListMenu) {
        super(battleCard, x, y, battleStage, cardListMenu, TokenType.HAND, false, true);
        resetCardGFX(getCard().getCardInfo().getCulture(), TokenType.HAND);
        setGlowOffsetX(-getGlowG().getWidth()/2f+getFrame().getWidth()/2f);
        setGlowOffsetY(-getGlowG().getHeight()/2f+getCardPic().getHeight()/2f);
        setOriginX(getWidth()/2);
        setOriginY(getHeight()/2);
    }

    @Override
    public void setGlows() {
        setGlowG(Farstar.ASSET_LIBRARY.get("images/tokens/glowG_D.png"));
        setGlowY(Farstar.ASSET_LIBRARY.get("images/tokens/glowY_D.png"));
    }

    @Override
    boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        if (button == 0) {
            if (getCard().getBattlePlayer() instanceof LocalBattlePlayer && !getBattleStage().getAbilityPicker().isActive() && ((!getBattleStage().getBattleScreen().getBattle().getCombatManager().isActive() && getBattleStage().getBattleScreen().getBattle().getWhoseTurn() == getCard().getBattlePlayer()) || getCard().isTactic() && getBattleStage().getBattleScreen().getBattle().getCombatManager().isTacticalPhase()) && !getBattleStage().getBattleScreen().getBattle().isEverythingDisabled()) {
                newFake(event, x, y, pointer, button, FakeTokenType.HAND);
                setHidden(true);
            }
            return false;
        } else {
            return super.touchDown(event, x, y, pointer, button);
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
        if (!isHidden()) {
            //Rotate
            oldMX = batch.getTransformMatrix().cpy();
            batch.setTransformMatrix(mx);
            //Draw
            if (getCard() != null) {
                drawGlows(batch);
                drawCardGFX(batch, getX(), getY(), getTokenType());
                if (!isBackside()) {
                    drawPortrait(batch);
                    getTokenDefense().draw(batch);
                    getTokenOffense().draw(batch);
                    getTokenPrice().draw(batch);
                }
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
        batch.end();
        getBattleStage().getBattleScreen().getShapeRenderer().begin(ShapeRenderer.ShapeType.Line);
        getBattleStage().getBattleScreen().getShapeRenderer().rect(getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), 1f, 1f, getRotation());
        getBattleStage().getBattleScreen().getShapeRenderer().end();
        batch.begin();
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

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    @Override
    public boolean isBackside() {
        return !(getCard().getBattlePlayer() instanceof LocalBattlePlayer || getBattleStage().getBattleScreen().getBattleType() == BattleType.SIMULATION);
    }

}
