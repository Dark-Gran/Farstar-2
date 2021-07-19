package com.darkgran.farstar.gui.tokens;

import com.darkgran.farstar.battle.players.BattleCard;
import com.darkgran.farstar.gui.battlegui.BattleStage;
import com.darkgran.farstar.gui.battlegui.CardListMenu;

public abstract class AnchoredToken extends ClickToken {
    private float anchorX;
    private float anchorY;

    public AnchoredToken(BattleCard battleCard, float x, float y, BattleStage battleStage, CardListMenu cardListMenu, TokenType tokenType, boolean noPics, boolean connectCard) {
        super(battleCard, x, y, battleStage, cardListMenu, tokenType, noPics, connectCard);
        this.anchorX = x;
        this.anchorY = y;
    }

    public void resetPosition() {
        setPosition(anchorX, anchorY);
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
