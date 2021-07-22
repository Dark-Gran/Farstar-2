package com.darkgran.farstar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.darkgran.farstar.gui.ColorPalette;
import com.darkgran.farstar.gui.NotificationManager;
import com.darkgran.farstar.gui.TableStage;
import com.darkgran.farstar.gui.YXQuestionBox;
import com.darkgran.farstar.util.SimpleVector2;

import static com.darkgran.farstar.Farstar.ASSET_LIBRARY;

public abstract class SuperScreen implements Screen {
    public final static boolean DEBUG_RENDER = false;
    private Farstar game;
    private static OrthographicCamera camera = new OrthographicCamera();
    private Viewport viewport = new ExtendViewport(Farstar.STAGE_WIDTH, Farstar.STAGE_HEIGHT, camera);
    private ShapeRenderer shapeRenderer = new ShapeRenderer();
    private TableStage tableStage;
    private YXQuestionBox screenConceder = null;
    private final NotificationManager notificationManager;
    private final PerfMeter perfMeter = new PerfMeter((float) (Farstar.STAGE_WIDTH*0.0885), (float) (Farstar.STAGE_HEIGHT*0.98), ColorPalette.MAIN);
    public enum CursorType {
        DEFAULT, AIM
    }
    //For some reason, there is a drastic loss of image quality between pixmap (which are what the Gdx.graphics.setCursor accepts) and png.
    //In-future: Look into Gdx.graphics and either find how to use the system cursor while keeping the quality OR make low-quality cursors for "low quality graphic settings" and use it only then.
    /*public static void switchCursor(CursorType cursorType) {
        Gdx.graphics.setCursor(getCursor(cursorType));
    }
    public static Cursor getCursor(CursorType cursorType) {
        switch (cursorType) {
            default:
            case DEFAULT:
                return Gdx.graphics.newCursor(ASSET_LIBRARY.get("images/cursor_default.png"), 0, 0);
            case AIM:
                return Gdx.graphics.newCursor(ASSET_LIBRARY.get("images/cursor_aim.png"), 16, 16);
        }
    }*/
    private Texture HDCursor;
    private int HDCursorOffsetX;
    private int HDCursorOffsetY;
    private boolean HDCursorInUse;
    public void switchCursor(CursorType cursorType) {
        switch (cursorType) {
            default:
            case DEFAULT:
                HDCursor = ASSET_LIBRARY.get("images/cursor_default.png");
                HDCursorOffsetX = 0;
                HDCursorOffsetY = 0;
                break;
            case AIM:
                HDCursor = ASSET_LIBRARY.get("images/cursor_aim.png");
                HDCursorOffsetX = 16;
                HDCursorOffsetY = 16;
                break;
        }
    }
    private void drawCursor(Batch batch) {
        boolean HDOn = false;
        if ( HDCursor != null) {
            SimpleVector2 coords = getMouseCoordinates();
            if (game.MWQ.isMouseInsideWindow()) {
                HDOn = true;
                batch.draw(HDCursor, coords.x - HDCursorOffsetX, Farstar.STAGE_HEIGHT - (coords.y - HDCursorOffsetY) - HDCursor.getHeight());
            } else {
                HDOn = false;
            }
        }
        if (HDOn != HDCursorInUse) {
            HDCursorInUse = HDOn;
            hideCursor(HDOn);
        }
    }
    public static void hideCursor(boolean hide) {
        if (hide) {
            Gdx.graphics.setCursor(Gdx.graphics.newCursor(ASSET_LIBRARY.get("images/cursor_transparent.png"), 0, 0));
        } else { Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow); }
    }
    public static SimpleVector2 getMouseCoordinates() {
        Vector3 pos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(pos);
        return new SimpleVector2(pos.x, Farstar.STAGE_HEIGHT-pos.y);
    }
    public static class ScreenSettings {
        private boolean helpEnabled = false;
        private boolean tableStageEnabled = true;
        private boolean netEnabled = false;
        private boolean tokenFramesEnabled = true;
        public ScreenSettings() {}
        public ScreenSettings(boolean helpEnabled, boolean tableStageEnabled, boolean netEnabled, boolean tokenFramesEnabled) {
            this.helpEnabled = helpEnabled;
            this.tableStageEnabled = tableStageEnabled;
            this.netEnabled = netEnabled;
            this.tokenFramesEnabled = tokenFramesEnabled;
        }
    }
    public void setTableStageEnabled(boolean tableStageEnabled) {
        screenSettings.tableStageEnabled = tableStageEnabled;
        if (tableStage != null) { tableStage.enableButtons(tableStageEnabled); }
    }
    public boolean isTableStageEnabled() { return screenSettings.tableStageEnabled; }
    public void setHelpEnabled(boolean helpEnabled) { screenSettings.helpEnabled = helpEnabled; }
    public boolean isHelpEnabled() { return screenSettings.helpEnabled; }
    public void setNetEnabled(boolean netEnabled) { screenSettings.netEnabled = netEnabled; }
    public boolean isNetEnabled() { return screenSettings.netEnabled; }
    public void setTokenFramesEnabled(boolean tokenFramesEnabled) { screenSettings.tokenFramesEnabled = tokenFramesEnabled; }
    public boolean isTokenFramesEnabled() { return screenSettings.tokenFramesEnabled; }
    public ScreenSettings getScreenSettings() { return screenSettings; }
    private ScreenSettings screenSettings;


    public SuperScreen(final Farstar game, NotificationManager notificationManager, ScreenSettings screenSettings) {
        this.screenSettings = screenSettings;
        this.game = game;
        this.notificationManager = notificationManager;
        camera.setToOrtho(false, Farstar.STAGE_WIDTH, Farstar.STAGE_HEIGHT);
        viewport.apply();
        camera.position.set((float) Farstar.STAGE_WIDTH/2,(float) Farstar.STAGE_HEIGHT/2,0);
        switchCursor(CursorType.DEFAULT);
    }

    protected void setTableMenu(TableStage tableStage) {
        this.tableStage = tableStage;
        this.tableStage.setViewport(viewport);
        if (!game.getInputMultiplexer().getProcessors().contains(tableStage, true)) {
            game.getInputMultiplexer().addProcessor(tableStage);
        }
    }

    public void userEscape() { }

    protected void drawContent(float delta, Batch batch) { }//for all screens except intro

    protected void drawMenus(float delta, Batch batch) { //for all screens except intro
        batch.begin();
        perfMeter.drawText(batch);
        if (screenConceder != null) {
            screenConceder.draw(batch, shapeRenderer);
        }
        batch.end();
    }

    protected void drawSigns(Batch batch) {
        notificationManager.drawAll(batch, shapeRenderer);
    }

    public static void switchFullscreen() {
        Graphics.DisplayMode currentMode = Gdx.graphics.getDisplayMode();
        if (Gdx.graphics.isFullscreen()) {
            Gdx.graphics.setWindowedMode(currentMode.width, currentMode.height);
        } else {
            Gdx.graphics.setFullscreenMode(currentMode);
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

        if (tableStage != null) { //should persist over all screens
            tableStage.drawBackground(game.batch, screenSettings.tableStageEnabled, screenSettings.netEnabled);
            if (screenSettings.tableStageEnabled) {
                tableStage.act(delta);
                tableStage.draw();
            }
        }

        drawMenus(delta, game.batch);

        game.batch.begin();
        game.batch.setColor(1, 1, 1, 1);
        drawContent(delta, game.batch);
        drawSigns(game.batch);
        drawCursor(game.batch);
        game.batch.end();

        update(delta);

    }

    protected void update(float delta) {
        notificationManager.update(delta);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set((float) Farstar.STAGE_WIDTH/2,(float) Farstar.STAGE_HEIGHT/2,0);
        camera.update();
    }

    @Override
    public void show() { }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void hide() { }

    @Override
    public void dispose() { }

    public Farstar getGame() { return game; }

    public OrthographicCamera getCamera() { return camera; }

    public Viewport getViewport() { return viewport; }

    public TableStage getTableMenu() { return tableStage; }

    public ShapeRenderer getShapeRenderer() { return shapeRenderer; }

    public NotificationManager getNotificationManager() { return notificationManager; }

    public YXQuestionBox getScreenConceder() { return screenConceder; }

    public void setScreenConceder(YXQuestionBox screenConceder) { this.screenConceder = screenConceder; }

    public boolean isConcederActive() { return screenConceder != null; }
}
