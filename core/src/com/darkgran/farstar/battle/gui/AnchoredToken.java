package com.darkgran.farstar.battle.gui;

import com.darkgran.farstar.battle.players.Card;

public abstract class AnchoredToken extends ClickToken {
    private final float originX;
    private final float originY;

    public AnchoredToken(Card card, float x, float y, BattleStage battleStage, CardListMenu cardListMenu) {
        super(card, x, y, battleStage, cardListMenu);
        this.originX = x;
        this.originY = y;
    }

    public void resetPosition() {
        setPosition(originX, originY);
    }

}
