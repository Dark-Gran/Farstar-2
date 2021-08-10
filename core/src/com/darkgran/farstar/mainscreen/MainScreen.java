package com.darkgran.farstar.mainscreen;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.darkgran.farstar.*;
import com.darkgran.farstar.gui.*;

public class MainScreen extends SuperScreen {
    private final MainScreenStage mainScreenStage = new MainScreenStage(getGame(), getViewport());

    public MainScreen(final Farstar game, ScreenSettings screenSettings, TableStage tableMenu) {
        super(game, screenSettings);
        setTableMenu(tableMenu);
        game.getInputMultiplexer().addProcessor(mainScreenStage);
        NotificationManager.getInstance().clear(Notification.NotificationType.MIDDLE);
    }

    @Override
    public void userEscape() {
        if (!isConcederActive()) {
            mainScreenStage.enableMainButtons(true);
            String txt = "GAME OVER?";
            String fontPath = "fonts/orbitron36.fnt";
            SimpleVector2 textWH = TextDrawer.getTextWH(AssetLibrary.getInstance().getAssetManager().get(fontPath, BitmapFont.class), txt);
            setScreenConceder(new YXQuestionBox(
                    ColorPalette.LIGHT,
                    ColorPalette.DARK,
                    fontPath,
                    txt,
                    Farstar.STAGE_WIDTH/2f - textWH.x/2,
                    Farstar.STAGE_HEIGHT/2f + textWH.y*2,
                    textWH.x,
                    textWH.y,
                    true,
                    mainScreenStage,
                    this::userEscape,
                    () -> System.exit(0)
            ));
        } else {
            mainScreenStage.enableMainButtons(false);
            hideScreenConceder();
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
