package com.darkgran.farstar.battle.gui.tokens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.battle.gui.BattleStage;
import com.darkgran.farstar.battle.gui.CardListMenu;
import com.darkgran.farstar.battle.players.cards.Card;

/**
 * Used for "card-zoom" etc.
 */
public class PrintToken extends Token {
    private Texture cardPic;

    public PrintToken(Card card, float x, float y, BattleStage battleStage, CardListMenu cardListMenu) {
        super(card, x, y, battleStage, cardListMenu, TokenType.PRINT, false);
        cardPic = Farstar.ASSET_LIBRARY.get("images/tokens/card_D.png");
        setTouchable(Touchable.disabled);
    }

    public void enable(Card card) {
        if (getCard() != card) {
            setCard(card);
            setPortrait(Farstar.ASSET_LIBRARY.get(Farstar.ASSET_LIBRARY.getPortraitName(card.getCardInfo(), getTokenType())));
            setFrame(Farstar.ASSET_LIBRARY.get(Farstar.ASSET_LIBRARY.getFrameName(card.getCardInfo(), getTokenType())));
            getTokenDefense().update();
            getTokenDefense().setPad(getTokenType());
            getTokenDefense().setupOffset();
            getTokenOffense().update();
            getTokenOffense().setPad(getTokenType());
            getTokenOffense().setupOffset();
            getTokenPrice().update();
            getTokenPrice().setPad(getTokenType());
            getTokenPrice().setupOffset();
        }
    }

    public void disable() {
        setCard(null);
    }

    @Override
    public void draw(Batch batch) {
        if (getCard() != null) {
            batch.draw(cardPic, getX(), getY());
            super.draw(batch);
        }
    }

    @Override
    protected void drawPortrait(Batch batch) {
        if (getPortrait() != null) { batch.draw(getPortrait(), getX(), getY()+cardPic.getHeight()-getPortrait().getHeight()+HandToken.PORTRAIT_OFFSET_Y); }
        if (getFrame() != null) { batch.draw(getFrame(), getX(), getY()+cardPic.getHeight()-getFrame().getHeight()+HandToken.PORTRAIT_OFFSET_Y); }
    }

}
