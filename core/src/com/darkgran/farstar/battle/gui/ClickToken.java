package com.darkgran.farstar.battle.gui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.darkgran.farstar.battle.players.Card;

public abstract class ClickToken extends Token {
    public ClickToken(Card card, float x, float y, BattleStage battleStage, CardListMenu cardListMenu) {
        super(card, x, y, battleStage, cardListMenu);
    }

    @Override
    public void setupListener() {
        super.setupListener();
        this.addListener(new InputListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                click(button);
            }
        });
    }

    public void click(int button) { }
}
