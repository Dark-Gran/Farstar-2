package com.darkgran.farstar.mainscreen;

import com.badlogic.gdx.Gdx;
import com.darkgran.farstar.*;

public class MainScreen extends SuperScreen {
    private final MainScreenStage mainScreenStage = new MainScreenStage(getGame(), getViewport());

    public MainScreen(final Farstar game, TableStage tableMenu, NotificationManager notificationManager) {
        super(game, notificationManager);
        setTableMenu(tableMenu);
        game.loadLibrary();
        game.getInputMultiplexer().addProcessor(mainScreenStage);
        Gdx.input.setCursorCatched(false);
    }

    @Override
    protected void userEscape() { //TODO
        if (!isConcederActive()) {
            setScreenConceder(new ScreenConceder(
                    ColorPalette.LIGHT,
                    ColorPalette.changeAlpha(ColorPalette.DARK, 0.5f),
                    "fonts/barlow30.fnt",
                    "",
                    50,
                    50,
                    50,
                    50
            ));
        }
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
