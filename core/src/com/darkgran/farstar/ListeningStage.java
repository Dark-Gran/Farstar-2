package com.darkgran.farstar;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

public abstract class ListeningStage extends Stage {
    private final Farstar game;

    public ListeningStage(final Farstar game, Viewport viewport) {
        super(viewport);
        this.game = game;
    }

    public Farstar getGame() { return game; }

    protected void setupListeners() {

    }

    protected boolean isConcederActive() {
        return getGame().getSuperScreen() != null && getGame().getSuperScreen().isConcederActive();
    }

}
