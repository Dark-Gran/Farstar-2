package com.darkgran.farstar.battle.gui;

import com.darkgran.farstar.battle.players.Card;

public class FakeToken extends Token { //temporary token for targeted deployment from shipyard
    private final Dragger dragger = new Dragger(this);

    public FakeToken(Card card, float x, float y, BattleStage battleStage, TokenMenu tokenMenu) {
        super(card, x, y, battleStage, tokenMenu);
        this.addListener(dragger.getInputListener());
    }

    public Dragger getDragger() { return dragger; }
}
