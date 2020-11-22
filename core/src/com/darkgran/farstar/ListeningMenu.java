package com.darkgran.farstar;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

public abstract class ListeningMenu extends Stage {
    private final Farstar game;

    public ListeningMenu(final Farstar game, Viewport viewport) {
        super(viewport);
        this.game = game;
    }

    public Farstar getGame() { return game; }

    public void setupListeners() {

    }

}
