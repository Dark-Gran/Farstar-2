package com.darkgran.farstar.mainscreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.SuperScreen;
import com.darkgran.farstar.TableStage;

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
        batch.draw(FSLogo, (float) (Farstar.STAGE_WIDTH/2-FSLogo.getWidth()/2), (float) (Farstar.STAGE_HEIGHT*0.78));
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
