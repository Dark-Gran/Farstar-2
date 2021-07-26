package com.darkgran.farstar.gui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.SuperScreen;
import com.darkgran.farstar.gui.tokens.AnchoredToken;
import com.darkgran.farstar.gui.tokens.FakeToken;
import com.darkgran.farstar.gui.tokens.Token;

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

    protected void drag(float x, float y) { //in-future: look into why the coordinates given by Libgdx seem to be lagging/glitching (huge differences between even and odd steps) - if possible, use them for dragging (instead of Gdx.input)
        SimpleVector2 coords = SuperScreen.getMouseCoordinates();
        token.setPosition(coords.x-token.getWidth()/2, Farstar.STAGE_HEIGHT-(coords.y+token.getHeight()/2));
    }

    protected void drop(float x, float y) {
        SimpleVector2 coords = SuperScreen.getMouseCoordinates();
        token.getBattleStage().processDrop(coords.x, Farstar.STAGE_HEIGHT-coords.y, token);
    }

    public Token getToken() { return token; }
}
