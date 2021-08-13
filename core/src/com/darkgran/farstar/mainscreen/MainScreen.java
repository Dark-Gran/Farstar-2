package com.darkgran.farstar.mainscreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.darkgran.farstar.*;
import com.darkgran.farstar.gui.*;

public class MainScreen extends SuperScreen {
    private final MainScreenStage mainScreenStage = new MainScreenStage(getGame(), getViewport());
    private float curtainAlpha = 1f;
    private static boolean fresh = true;

    public MainScreen(final Farstar game, ScreenSettings screenSettings, TableStage tableMenu) {
        super(game, screenSettings);
        setTableMenu(tableMenu);
        game.getInputMultiplexer().addProcessor(mainScreenStage);
        NotificationManager.getInstance().clear(Notification.NotificationType.MIDDLE);
        if (fresh) { blockAllButtons(true); }
    }

    @Override
    public void userEscape() {
        if (!isBlockTableButtonsOn()) {
            if (!isConcederActive()) {
                mainScreenStage.enableMainButtons(true);
                mainScreenStage.hideMainButtons(true);
                String txt = "GAME OVER?";
                String fontPath = "fonts/orbitron36.fnt";
                SimpleVector2 textWH = TextDrawer.getTextWH(AssetLibrary.getInstance().getAssetManager().get(fontPath, BitmapFont.class), txt);
                setScreenConceder(new YXQuestionBox(
                        ColorPalette.LIGHT,
                        ColorPalette.DARK,
                        fontPath,
                        txt,
                        Farstar.STAGE_WIDTH / 2f - textWH.x / 2,
                        Farstar.STAGE_HEIGHT / 2f + textWH.y * 2,
                        textWH.x,
                        textWH.y,
                        true,
                        mainScreenStage,
                        this::userEscape,
                        () -> System.exit(0)
                ));
            } else {
                mainScreenStage.enableMainButtons(false);
                mainScreenStage.hideMainButtons(false);
                hideScreenConceder();
            }
        }
    }

    private void blockAllButtons(boolean block) {
        setBlockTableButtons(block);
        mainScreenStage.enableMainButtons(block);
        mainScreenStage.disableWebButton(block);
    }

    @Override
    protected void update(float delta) {
        super.update(delta);
        if (fresh && curtainAlpha > 0) {
            curtainAlpha -= delta;
            if (curtainAlpha < 0) {
                curtainAlpha = 0;
                blockAllButtons(false);
                fresh = false;
                NotificationManager.getInstance().newNotification(Notification.NotificationType.BOT_LEFT, "Alpha Warning: Most Features Not Available.", 8);
            }
        }
    }

    @Override
    protected void drawSigns(Batch batch) {
        super.drawSigns(batch);
        if (fresh && curtainAlpha > 0) {
            batch.end();
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            getShapeRenderer().begin(ShapeRenderer.ShapeType.Filled);
            getShapeRenderer().setColor(ColorPalette.changeAlpha(Color.BLACK, curtainAlpha));
            float ratio = (float) Farstar.STAGE_WIDTH / Farstar.STAGE_HEIGHT;
            float actualRatio = (float) Gdx.graphics.getWidth() / Gdx.graphics.getHeight();
            float scaleX = 1.05f;
            float scaleY = 1.05f;
            if (actualRatio != ratio) {
                if (actualRatio > ratio) {
                    scaleX += (actualRatio-ratio)/2f;
                } else {
                    scaleY += (ratio-actualRatio)/2f;
                }
            }
            float width = Farstar.STAGE_WIDTH*scaleX;
            float height = Farstar.STAGE_HEIGHT*scaleY;
            getShapeRenderer().rect(Farstar.STAGE_WIDTH/2f - width/2f, Farstar.STAGE_HEIGHT/2f - height/2f, width, height);
            getShapeRenderer().setColor(Color.WHITE);
            getShapeRenderer().end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
            batch.begin();
        }
    }

    @Override
    protected void drawMenus(float delta, Batch batch) {
        super.drawMenus(delta, batch);
        if (!isBlockTableButtonsOn()) { mainScreenStage.act(delta); }
        mainScreenStage.draw();
    }

    @Override
    public void dispose() {
        getGame().getInputMultiplexer().removeProcessor(mainScreenStage);
        mainScreenStage.dispose();
        super.dispose();
    }
}
