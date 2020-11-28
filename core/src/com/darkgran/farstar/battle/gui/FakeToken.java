package com.darkgran.farstar.battle.gui;

import com.darkgran.farstar.battle.players.Card;

public class FakeToken extends ShipToken {
    public FakeToken(Card card, float x, float y, BattleStage battleStage) {
        super(card, x, y, battleStage);
    }

    //TODO DragNDrop (starts dragged on creation)
}
