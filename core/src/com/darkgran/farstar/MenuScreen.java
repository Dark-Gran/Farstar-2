package com.darkgran.farstar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;

public class MenuScreen extends SuperScreen {
    private final MainMenu menu = new MainMenu(getGame(), getViewport());

    public MenuScreen(final Farstar game, TableMenu tableMenu) {
        super(game);
        setTableMenu(tableMenu);
        game.getInputMultiplexer().addProcessor(menu);
        Gdx.input.setCursorCatched(false);
    }

    @Override
    public void drawScreen(float delta, Batch batch) {
        menu.act(Gdx.graphics.getDeltaTime());
        menu.draw();
    }

    @Override
    public void dispose() {
        getGame().getInputMultiplexer().removeProcessor(menu);
        menu.dispose();
    }
}
