package com.darkgran.farstar.battle.gui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.darkgran.farstar.battle.players.Card;

public class YardToken extends ShipToken {
    public YardToken(Card card, float x, float y, BattleStage battleStage) {
        super(card, x, y, battleStage);
    }

    @Override
    public void setupListener() {
        this.addListener(new ClickListener()
        {
            @Override
            public void clicked(InputEvent event, float x, float y)
            {
                getBattleStage().setFakeToken(new FakeToken(getCard(), getX(), getY(), getBattleStage()));
            }
        });
    }

}
