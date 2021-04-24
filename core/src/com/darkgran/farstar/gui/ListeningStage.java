package com.darkgran.farstar.gui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.darkgran.farstar.Farstar;

public abstract class ListeningStage extends Stage {
    private final Farstar game;

    public ListeningStage(final Farstar game, Viewport viewport) {
        super(viewport);
        this.game = game;
    }

    public Farstar getGame() { return game; }

    @Deprecated
    protected void setupListeners() {

    }

}
