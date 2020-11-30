package com.darkgran.farstar.battle.gui;

import com.darkgran.farstar.battle.players.Card;

public class HandToken extends AnchoredToken {

    public HandToken(Card card, float x, float y, BattleStage battleStage, TokenMenu tokenMenu) {
        super(card, x, y, battleStage, tokenMenu);
        setDragger(new ManagedDragger(this, battleStage.getBattleScreen().getBattle().getRoundManager(), false));
        this.addListener(getDragger().getInputListener());
    }

}
