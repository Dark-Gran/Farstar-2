package com.darkgran.farstar;

import com.badlogic.gdx.Gdx;

public class MenuScreen extends SuperScreen {
    private final MainMenu menu = new MainMenu(getGame(), getViewport());

    public MenuScreen(final Farstar game, TableMenu tableMenu) {
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
