package com.darkgran.farstar.battle.gui;

import com.darkgran.farstar.battle.players.Card;

public class FakeToken extends Token { //temporary token for targeted deployment from shipyard

    public FakeToken(Card card, float x, float y, BattleStage battleStage, TokenMenu tokenMenu) {
        super(card, x, y, battleStage, tokenMenu);
        setDragger(new Dragger(this));
        this.addListener(getDragger().getInputListener());
    }

}
