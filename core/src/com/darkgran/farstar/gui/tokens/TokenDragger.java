package com.darkgran.farstar.gui.tokens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.darkgran.farstar.gui.Dragger;
import com.darkgran.farstar.gui.Draggable;
import com.darkgran.farstar.gui.ListeningStage;


public class TokenDragger extends Dragger {

    public TokenDragger(Draggable draggable, ListeningStage listeningStage) {
        super(draggable, listeningStage);
    }

    @Override
    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
        boolean orig = super.touchDown(event, x, y, pointer, button);
        if (button == 1) {
            if (getDraggable() instanceof AnchoredToken) { ((AnchoredToken) getDraggable()).resetPosition(); }
            if (getDraggable() instanceof FakeToken) { ((Token) getDraggable()).destroy(); }
            return getDraggable() instanceof FakeToken;
        }
        return orig;
    }

    @Override
    public void drop(float x, float y) {
        super.drop(x ,y);
        if (!canceled) {
            ((Token) getDraggable()).getBattleStage().processDrop(x, y, (Token) getDraggable());
        }
    }

}
