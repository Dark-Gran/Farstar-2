package com.darkgran.farstar.battle.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.darkgran.farstar.Farstar;

public class Dragger extends InputListener {
    private final Token token;
    public boolean canceled = false;

    @Override
    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
        if (button == 1) {
            event.getStage().removeTouchFocus(this, event.getListenerActor(), event.getTarget(), event.getPointer(), event.getButton());
            if (token instanceof AnchoredToken) { ((AnchoredToken) token).resetPosition(); }
            canceled = true;
            return token instanceof FakeToken;
        } else {
            canceled = false;
        }
        return true;
    }

    @Override
    public void touchDragged (InputEvent event, float x, float y, int pointer) {
        if (!canceled) { drag(x, y); }
    }

    @Override
    public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
        if (button == 0 && !canceled) {
            drop(x, y);
        } else { if (token instanceof FakeToken) { token.destroy(); } }
    }

    public Dragger(Token token) {
        this.token = token;
    }

    public void drag(float x, float y) {
        token.setPosition(Gdx.input.getX()-token.getWidth()/2, Farstar.STAGE_HEIGHT-(Gdx.input.getY()+token.getHeight()/2));
    }

    public void drop(float x, float y) {
        token.getBattleStage().processDrop(Gdx.input.getX(), Farstar.STAGE_HEIGHT-Gdx.input.getY(), token);
    }

    public Token getToken() { return token; }
}
