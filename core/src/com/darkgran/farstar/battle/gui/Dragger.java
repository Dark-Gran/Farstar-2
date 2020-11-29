package com.darkgran.farstar.battle.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.darkgran.farstar.Farstar;

public class Dragger {
    private final Token token;
    private final InputListener inputListener = new InputListener() {
        @Override
        public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
            return true;
        }

        @Override
        public void touchDragged (InputEvent event, float x, float y, int pointer) { drag(x, y); }

        @Override
        public void touchUp (InputEvent event, float x, float y, int pointer, int button) { drop(x, y); }
    };

    public Dragger(Token token) {
        this.token = token;
    }

    public void drag(float x, float y) {
        token.setPosition(Gdx.input.getX(), Farstar.STAGE_HEIGHT-Gdx.input.getY());
    }

    public void drop(float x, float y) {
        token.getBattleStage().processDrop(Gdx.input.getX(), Farstar.STAGE_HEIGHT-Gdx.input.getY(), token);
    }

    public InputListener getInputListener() { return inputListener; }

    public Token getToken() { return token; }
}
