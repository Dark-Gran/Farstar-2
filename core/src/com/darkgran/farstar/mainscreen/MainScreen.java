package com.darkgran.farstar.mainscreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.darkgran.farstar.Farstar;
import com.darkgran.farstar.SuperScreen;
import com.darkgran.farstar.TableStage;

public class MainScreen extends SuperScreen {
    private final MainScreenStage menu = new MainScreenStage(getGame(), getViewport());
    private final Texture FSLogo = new Texture("images/FSLogo.png");
    private final VersionInfo versionInfo = new VersionInfo((float) (Farstar.STAGE_WIDTH*0.85), (float) (Farstar.STAGE_HEIGHT*0.98), new Color(0.329f, 0.553f, 1, 1));
    private final PerfMeter perfMeter = new PerfMeter((float) (Farstar.STAGE_WIDTH*0.15), (float) (Farstar.STAGE_HEIGHT*0.98), new Color(0.329f, 0.553f, 1, 1));

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
        versionInfo.draw(batch);
        perfMeter.draw(batch);
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
        versionInfo.dispose();
        perfMeter.dispose();
    }
}
