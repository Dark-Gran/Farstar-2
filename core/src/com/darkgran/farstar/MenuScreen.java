package com.darkgran.farstar;

import com.badlogic.gdx.Gdx;
import com.darkgran.farstar.ui.MainMenu;
import com.darkgran.farstar.ui.TableMenu;

public class MenuScreen extends SuperScreen {
    private final MainMenu menu;

    public MenuScreen(final Farstar game, TableMenu tableMenu) {
        super(game);
        this.tableMenu = tableMenu;
        menu = new MainMenu(viewport);
        Gdx.input.setInputProcessor(menu);
        Gdx.input.setCursorCatched(false);
    }

    @Override
    public void drawScreen() {
        menu.act(Gdx.graphics.getDeltaTime());
        menu.draw();
    }

    @Override
    public void dispose() {
        menu.dispose();
    }
}
