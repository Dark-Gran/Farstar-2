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
    private static final float PORTRAIT_OFFSET_Y = -16f;
    private Texture cardPic;

    public HandToken(Card card, float x, float y, BattleStage battleStage, CardListMenu cardListMenu) {
        super(card, x, y, battleStage, cardListMenu, TokenType.HAND, false);
        setDragger(new ManagedDragger(this, battleStage.getBattleScreen().getBattle().getRoundManager(), false){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (isEnabled() && button == 0 && getCard().getCardInfo().getCardType() == CardType.BLUEPRINT) {
                    getCardListMenu().getPlayer().getFleet().getFleetMenu().setPredictEnabled(true);
                }
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        this.addListener(getDragger());
        cardPic = Farstar.ASSET_LIBRARY.get("images/tokens/card.png");
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
