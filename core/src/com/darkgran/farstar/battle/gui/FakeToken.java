package com.darkgran.farstar.battle.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.battle.players.Card;

public class FakeToken extends Token {
    private final InputListener inputListener = new InputListener() {
        @Override
        public void touchDragged (InputEvent event, float x, float y, int pointer) { drag(x, y); }

        @Override
        public void touchUp (InputEvent event, float x, float y, int pointer, int button) { drop(); }
    };

    public FakeToken(Card card, float x, float y, BattleStage battleStage, TokenMenu tokenMenu) {
        super(card, x, y, battleStage, tokenMenu);
        this.addListener(inputListener);
    }

    private void drag(float x, float y) {
        setPosition(Gdx.input.getX(), Farstar.STAGE_HEIGHT-Gdx.input.getY());
    }

    private void drop() {
        System.out.println("Dropped!"); //TODO deployment
        destroy();
    }

    private void destroy() {
        this.remove();
        getBattleStage().setFakeToken(null);
    }

    public InputListener getInputListener() { return inputListener; }
}
