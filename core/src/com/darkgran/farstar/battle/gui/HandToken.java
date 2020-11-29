package com.darkgran.farstar.battle.gui;

import com.darkgran.farstar.battle.players.Card;

public class HandToken extends Token {
    private final Dragger dragger = new Dragger(this);
    private final float originX;
    private final float originY;

    public HandToken(Card card, float x, float y, BattleStage battleStage, TokenMenu tokenMenu) {
        super(card, x, y, battleStage, tokenMenu);
        this.originX = x;
        this.originY = y;
        this.addListener(dragger.getInputListener());
    }

    public void resetPosition() {
        setPosition(originX, originY);
    }

}
