package com.darkgran.farstar.gui.tokens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.SuperScreen;
import com.darkgran.farstar.gui.Dragger;
import com.darkgran.farstar.gui.SimpleVector2;

public class TokenDragger extends Dragger {
    private final Token token;


    public TokenDragger(Token token) {
        this.token = token;
    }

    @Override
    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
        if (button == 1) {
            event.getStage().removeTouchFocus(this, event.getListenerActor(), event.getTarget(), event.getPointer(), event.getButton());
            canceled = true;
            if (token instanceof AnchoredToken) { ((AnchoredToken) token).resetPosition(); }
            return token instanceof FakeToken;
        } else {
            canceled = false;
        }
        return true;
    }

    @Override
    public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
        super.touchUp(event, x, y, pointer, button);
        if ((button != 0 || canceled) && token instanceof FakeToken)  {
            token.destroy();
        }
    }

    @Override
    protected void drag(float x, float y) { //in-future: look into why the coordinates given by Libgdx seem to be lagging/glitching (huge differences between even and odd steps) - if possible, use them for dragging (instead of Gdx.input)
        SimpleVector2 coords = SuperScreen.getMouseCoordinates();
        token.setPosition(coords.x-token.getWidth()/2, Farstar.STAGE_HEIGHT-(coords.y+token.getHeight()/2));
    }

    @Override
    protected void drop(float x, float y) {
        SimpleVector2 coords = SuperScreen.getMouseCoordinates();
        token.getBattleStage().processDrop(coords.x, Farstar.STAGE_HEIGHT-coords.y, token);
    }

    public Token getToken() { return token; }

}
