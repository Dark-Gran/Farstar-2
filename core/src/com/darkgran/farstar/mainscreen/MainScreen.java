package com.darkgran.farstar.mainscreen;

import com.badlogic.gdx.Gdx;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.SuperScreen;
import com.darkgran.farstar.TableStage;

public class MainScreen extends SuperScreen {
    private final MainScreenStage mainScreenStage = new MainScreenStage(getGame(), getViewport());

    public MainScreen(final Farstar game, TableStage tableMenu) {
        super(game);
        setTableMenu(tableMenu);
        game.loadLibrary();
        game.getInputMultiplexer().addProcessor(mainScreenStage);
        Gdx.input.setCursorCatched(false);
    }

    @Override
    protected void drawMenus(float delta) {
        mainScreenStage.act(delta);
        mainScreenStage.draw();
    }

    @Override
    public void dispose() {
        getGame().getInputMultiplexer().removeProcessor(mainScreenStage);
        mainScreenStage.dispose();
        super.dispose();
    }
}
