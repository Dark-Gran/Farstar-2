package com.darkgran.farstar.battle.gui;

import com.darkgran.farstar.battle.players.Card;

public class AnchoredToken extends Token {
    private final float originX;
    private final float originY;

    public AnchoredToken(Card card, float x, float y, BattleStage battleStage, TokenMenu tokenMenu) {
        super(card, x, y, battleStage, tokenMenu);
        this.originX = x;
        this.originY = y;
    }

    public void resetPosition() {
        setPosition(originX, originY);
    }

}
