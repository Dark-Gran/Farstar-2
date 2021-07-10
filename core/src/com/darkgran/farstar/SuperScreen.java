package com.darkgran.farstar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.darkgran.farstar.gui.NotificationManager;
import com.darkgran.farstar.gui.TableStage;
import com.darkgran.farstar.gui.YXQuestionBox;

import static com.darkgran.farstar.Farstar.ASSET_LIBRARY;

public abstract class SuperScreen implements Screen {
    public final static boolean DEBUG_RENDER = false;
    private Farstar game;
    private OrthographicCamera camera = new OrthographicCamera();
    private Viewport viewport = new ExtendViewport(Farstar.STAGE_WIDTH, Farstar.STAGE_HEIGHT, camera);
    private ShapeRenderer shapeRenderer = new ShapeRenderer();
    private TableStage tableStage;
    private YXQuestionBox screenConceder = null;
    private final NotificationManager notificationManager;
    private final PerfMeter perfMeter = new PerfMeter((float) (Farstar.STAGE_WIDTH*0.0885), (float) (Farstar.STAGE_HEIGHT*0.98), ColorPalette.MAIN);
    public enum CursorType {
        DEFAULT, AIM;
    }
    public static void switchCursor(CursorType cursorType) {
        Cursor cursor = Gdx.graphics.newCursor(ASSET_LIBRARY.get("images/cursor_default.png"), 0, 0);
        switch (cursorType) {
            case AIM:
                cursor = Gdx.graphics.newCursor(ASSET_LIBRARY.get("images/cursor_aim.png"), 16, 16);
                break;
        }
        Gdx.graphics.setCursor(cursor);
    }
    private final Vector3 mouseInWorld3D = new Vector3();

    public SuperScreen(final Farstar game, NotificationManager notificationManager) {
        this.game = game;
        this.notificationManager = notificationManager;
        camera.setToOrtho(false, Farstar.STAGE_WIDTH, Farstar.STAGE_HEIGHT);
        viewport.apply();
        camera.position.set((float) Farstar.STAGE_WIDTH/2,(float) Farstar.STAGE_HEIGHT/2,0);
        switchCursor(CursorType.DEFAULT);
    }

    public void refreshMouseInWorld() {
        mouseInWorld3D.x = Gdx.input.getX();
        mouseInWorld3D.y = Gdx.input.getY();
        mouseInWorld3D.z = 0;
        getCamera().unproject(mouseInWorld3D);
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
        refreshMouseInWorld();

        if (tableStage != null) { //should persist over all screens
            tableStage.drawBackground(game.batch);
            tableStage.act(delta);
            tableStage.draw();
        }

        drawMenus(delta, game.batch);

        game.batch.begin();
        game.batch.setColor(1, 1, 1, 1);
        drawContent(delta, game.batch);
        drawSigns(game.batch);
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

    public Vector3 getMouseInWorld3D() {
        return mouseInWorld3D;
    }
}
