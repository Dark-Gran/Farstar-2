package com.darkgran.farstar;

import com.badlogic.gdx.Gdx;

public class MainScreen extends SuperScreen {
    private final MainScreenStage menu = new MainScreenStage(getGame(), getViewport());

    public MainScreen(final Farstar game, TableStage tableMenu) {
        super(game);
        setTableMenu(tableMenu);
        game.getInputMultiplexer().addProcessor(menu);
        Gdx.input.setCursorCatched(false);
    }

    @Override
    public void drawMenus(float delta) {
        menu.act(delta);
        menu.draw();
    }

    @Override
    public void dispose() {
        getGame().getInputMultiplexer().removeProcessor(menu);
        menu.dispose();
    }
}
