package com.darkgran.farstar.mainscreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.darkgran.farstar.*;
import com.darkgran.farstar.util.SimpleVector2;
import com.darkgran.farstar.util.TextDrawer;

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
            mainScreenStage.disableMainButtons();
            SimpleVector2 textWH = TextDrawer.getTextWH(Farstar.ASSET_LIBRARY.getAssetManager().get("fonts/barlow30.fnt", BitmapFont.class), "ASD");
            setScreenConceder(new YXQuestionBox(
                    ColorPalette.LIGHT,
                    ColorPalette.changeAlpha(ColorPalette.DARK, 0.5f),
                    "fonts/barlow30.fnt",
                    "ASD",
                    Farstar.STAGE_WIDTH/2f,
                    Farstar.STAGE_HEIGHT/2f,
                    textWH.getX(),
                    textWH.getY()
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
