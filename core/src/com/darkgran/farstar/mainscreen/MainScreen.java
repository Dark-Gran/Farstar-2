package com.darkgran.farstar.mainscreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.darkgran.farstar.*;
import com.darkgran.farstar.gui.*;
import com.darkgran.farstar.util.SimpleVector2;

public class MainScreen extends SuperScreen {
    private final MainScreenStage mainScreenStage = new MainScreenStage(getGame(), getViewport());

    public MainScreen(final Farstar game, TableStage tableMenu, NotificationManager notificationManager, ScreenSettings screenSettings) {
        super(game, notificationManager, screenSettings);
        setTableMenu(tableMenu);
        game.loadLibrary();
        game.getInputMultiplexer().addProcessor(mainScreenStage);
        Gdx.input.setCursorCatched(false);
    }

    @Override
    public void userEscape() {
        if (!isConcederActive()) {
            mainScreenStage.enableMainButtons(true);
            String txt = "GAME OVER?";
            String fontPath = "fonts/orbitron36.fnt";
            SimpleVector2 textWH = TextDrawer.getTextWH(Farstar.ASSET_LIBRARY.getAssetManager().get(fontPath, BitmapFont.class), txt);
            setScreenConceder(new YXQuestionBox(
                    ColorPalette.LIGHT,
                    ColorPalette.DARK,
                    fontPath,
                    txt,
                    Farstar.STAGE_WIDTH/2f - textWH.getX()/2,
                    Farstar.STAGE_HEIGHT/2f + textWH.getY()/2 + textWH.getY()*2,
                    textWH.getX(),
                    textWH.getY(),
                    true,
                    mainScreenStage,
                    this::userEscape,
                    () -> System.exit(0)
            ));
        } else {
            mainScreenStage.enableMainButtons(false);
            getScreenConceder().dispose();
            setScreenConceder(null);
        }
    }

    @Override
    protected void drawMenus(float delta, Batch batch) {
        super.drawMenus(delta, batch);
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
