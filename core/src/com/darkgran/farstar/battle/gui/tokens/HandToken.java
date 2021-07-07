package com.darkgran.farstar.battle.gui.tokens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.battle.gui.BattleStage;
import com.darkgran.farstar.battle.gui.CardListMenu;
import com.darkgran.farstar.battle.players.cards.Card;
import com.darkgran.farstar.battle.players.cards.CardType;

public class HandToken extends AnchoredToken {
    public enum HandState {
        DOWN, UP;
    }
    private static final float PORTRAIT_OFFSET_Y = -16f;
    private Texture cardPic;
    private HandState currentState = HandState.DOWN;
    private HandState nextState = currentState;


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
        cardPic = Farstar.ASSET_LIBRARY.get("images/tokens/card.png");
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
            }
            getTokenDefense().setPad(tokenType);
            getTokenOffense().setPad(tokenType);
            getTokenPrice().setPad(tokenType);
            setPosition(getX(), getY());
            currentState = nextState;
        }
    }

    @Override
    public void draw(Batch batch) {
        batch.draw(cardPic, getX(), getY());
        super.draw(batch);
    }

    @Override
    protected void drawPortrait(Batch batch) {
        if (getPortrait() != null) { batch.draw(getPortrait(), getX(), getY()+cardPic.getHeight()-getPortrait().getHeight()+PORTRAIT_OFFSET_Y); }
        if (getFrame() != null) { batch.draw(getFrame(), getX(), getY()+cardPic.getHeight()-getFrame().getHeight()+PORTRAIT_OFFSET_Y); }
    }

    @Override
    public void destroy() {
        super.destroy();
        getCardListMenu().getPlayer().getFleet().getFleetMenu().setPredictEnabled(false);
    }

}
