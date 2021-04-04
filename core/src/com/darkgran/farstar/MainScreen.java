package com.darkgran.farstar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class MainScreen extends SuperScreen {
    private final MainScreenStage menu = new MainScreenStage(getGame(), getViewport());
    private final Texture FSLogo = new Texture("images/FSLogo.png");

    public MainScreen(final Farstar game, TableStage tableMenu) {
        super(game);
        setTableMenu(tableMenu);
        game.loadLibrary();
        game.getInputMultiplexer().addProcessor(menu);
        Gdx.input.setCursorCatched(false);
    }

    @Override
    protected void drawContent(float delta, Batch batch) {
        batch.draw(FSLogo, Farstar.STAGE_WIDTH/2f-FSLogo.getWidth()/2f, Farstar.STAGE_HEIGHT*0.78f);
    }

    @Override
    protected void drawMenus(float delta) {
        menu.act(delta);
        menu.draw();
    }

    @Override
    public void dispose() {
        getGame().getInputMultiplexer().removeProcessor(menu);
        menu.dispose();
        FSLogo.dispose();
    }
}
