package com.darkgran.farstar.battle.gui;

import com.darkgran.farstar.battle.players.Card;

public class HandToken extends AnchoredToken {

    public HandToken(Card card, float x, float y, BattleStage battleStage, TokenMenu tokenMenu) {
        super(card, x, y, battleStage, tokenMenu);
        setDragger(new TurnDragger(this, battleStage.getBattleScreen().getBattle().getCombatManager()));
        this.addListener(getDragger().getInputListener());
    }

}
