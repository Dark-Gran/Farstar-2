package com.darkgran.farstar.gui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.SuperScreen;

public abstract class ListeningStage extends Stage {
    private final Farstar game;
    private Draggable mainDrag;

    public ListeningStage(final Farstar game, Viewport viewport) {
        super(viewport);
        this.game = game;
    }

    @Override
    public void act(float delta) {
        if (mainDrag != null) {
            SimpleVector2 coords = SuperScreen.getMouseCoordinates();
            mainDrag.getDragger().drag(coords.x, coords.y);
        }
        super.act(delta);
    }

    public Farstar getGame() { return game; }

    public Draggable getMainDrag() {
        return mainDrag;
    }

    public void setMainDrag(Draggable mainDrag) {
        this.mainDrag = mainDrag;
    }
}
