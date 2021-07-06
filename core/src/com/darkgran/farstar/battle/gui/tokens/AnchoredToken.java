package com.darkgran.farstar.battle.gui.tokens;

import com.darkgran.farstar.battle.gui.BattleStage;
import com.darkgran.farstar.battle.gui.CardListMenu;
import com.darkgran.farstar.battle.players.cards.Card;

public abstract class AnchoredToken extends ClickToken {
    private float anchorX;
    private float anchorY;

    public AnchoredToken(Card card, float x, float y, BattleStage battleStage, CardListMenu cardListMenu, TokenType tokenType, boolean noPics) {
        super(card, x, y, battleStage, cardListMenu, tokenType, noPics);
        this.anchorX = x;
        this.anchorY = y;
    }

    public void resetPosition() {
        setPosition(anchorX, anchorY);
        setPads();
    }

    public void setNewAnchor(float x, float y) {
        anchorX = x;
        anchorY = y;
    }

    public float getAnchorX() { return anchorX; }

    public void setAnchorX(float anchorX) { this.anchorX = anchorX; }

    public float getAnchorY() { return anchorY; }

    public void setAnchorY(float anchorY) { this.anchorY = anchorY; }

}
