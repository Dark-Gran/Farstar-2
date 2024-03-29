package com.darkgran.farstar.gui.tokens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.darkgran.farstar.battle.BattleType;
import com.darkgran.farstar.battle.players.BattleCard;
import com.darkgran.farstar.battle.players.LocalBattlePlayer;
import com.darkgran.farstar.gui.battlegui.BattleStage;
import com.darkgran.farstar.gui.battlegui.CardListMenu;
import com.darkgran.farstar.gui.battlegui.HandMenu;
import com.darkgran.farstar.util.DeltaCounter;
import com.darkgran.farstar.gui.SimpleVector2;

public abstract class TokenZoom extends PrintToken {
    private final DeltaCounter counter;
    private boolean enabled = true;
    private boolean counting;
    private boolean hidden = true;
    private final Explainer explainer = new Explainer();
    private boolean showExplainer = false;

    public TokenZoom(BattleCard battleCard, float x, float y, BattleStage battleStage, CardListMenu cardListMenu, float counterCap) {
        super(battleCard, x, y, battleStage, cardListMenu, false);
        counter = new DeltaCounter(false, counterCap, 0);
    }

    public void update(float delta) {
        counter.update(delta);
        if (counting && !counter.isEnabled()) {
            counting = false;
            showExplainer = true;
            explainer.refreshText(getCard());
            explainer.setShiftedPosition(getX(), getY());
        }
    }

    public void deactivate(boolean rightClick) {
        if (!rightClick || !(getTargetType() != null && (getTargetType() == TokenType.HAND || getTargetType() == TokenType.JUNK))) {
            deactivate();
        }
    }

    public void deactivate() {
        enabled = false;
        hidden = true;
        hideExplainer();
    }

    public void reactivate() {
        enabled = true;
        if (getCard() != null && getTargetType() != null) {
            hidden = false;
            counter.setEnabled(true);
            counting = true;
            if (getCard().getToken() instanceof HandToken) {
                ((HandToken) getCard().getToken()).setHidden(true);
            }
        }
    }

    @Override
    public void enable(BattleCard battleCard, TokenType targetType, SimpleVector2 targetXY) {
        if (getBattleStage().isEnabled() && getCard() != battleCard && (targetType != TokenType.HAND || battleCard.getBattlePlayer() instanceof LocalBattlePlayer || getBattleStage().getBattleScreen().getBattleType() == BattleType.SIMULATION)) {
            counter.setEnabled(true);
            counting = true;
            hidden = false;
            super.enable(battleCard, targetType, targetXY);
            if (targetType != TokenType.JUNK && battleCard.getToken().getCardListMenu() instanceof HandMenu) {
                ((HandMenu) battleCard.getToken().getCardListMenu()).setHandState(HandMenu.HandMenuState.UP);
                if (!Gdx.input.isButtonPressed(Input.Buttons.LEFT) && battleCard.getBattlePlayer() instanceof LocalBattlePlayer && battleCard.getToken() != null) { ((HandToken) battleCard.getToken()).setHidden(true); }
            }
        }
    }

    @Override
    public void disable() {
        //in-future: consider split (TokenZoom -> _Preview_ x Herald) class instead of "asking parent if he's a child", which probably isn't the best practice
        if (!(this instanceof Herald) && getCard() != null && getCard().getToken().getTokenType() != TokenType.JUNK && getCard().getToken().getCardListMenu() instanceof HandMenu) {
            ((HandMenu) getCard().getToken().getCardListMenu()).setHandState(HandMenu.HandMenuState.IDLE);
            if (getCard().getBattlePlayer() instanceof LocalBattlePlayer && !Gdx.input.isButtonPressed(Input.Buttons.LEFT)) { ((HandToken) getCard().getToken()).setHidden(false); }
        }
        super.disable();
        hideExplainer();
    }

    private void hideExplainer() {
        showExplainer = false;
        counter.setEnabled(false);
        counter.setAccumulator(0);
        counting = false;
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        if (explainer != null) { explainer.setShiftedPosition(x, y); }
    }

    @Override
    public void draw(Batch batch) {
        if (isEnabled() && !hidden && getCard() != null && getTargetType() != null && getTargetXY() != null) {
            super.draw(batch);
            if (showExplainer && !explainer.getText().equals("")) { explainer.draw(batch, getBattleStage().getBattleScreen().getShapeRenderer()); }
        }
    }

    public DeltaCounter getCounter() {
        return counter;
    }

    public boolean isCounting() {
        return counting;
    }

    public void setCounting(boolean counting) {
        this.counting = counting;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Explainer getExplainer() {
        return explainer;
    }

    public boolean isShowExplainer() {
        return showExplainer;
    }

    public void setShowExplainer(boolean showExplainer) {
        this.showExplainer = showExplainer;
    }

}
