package com.darkgran.farstar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public abstract class SuperScreen implements Screen {
    private Farstar game;
    private OrthographicCamera camera = new OrthographicCamera();
    private Viewport viewport = new ExtendViewport(Farstar.STAGE_WIDTH, Farstar.STAGE_HEIGHT, camera);
    private ShapeRenderer shapeRenderer = new ShapeRenderer();
    private TableStage tableStage;
    private final NotificationManager notificationManager;

    public SuperScreen(final Farstar game, NotificationManager notificationManager) {
        this.game = game;
        this.notificationManager = notificationManager;
        camera.setToOrtho(false, Farstar.STAGE_WIDTH, Farstar.STAGE_HEIGHT);
        viewport.apply();
        camera.position.set((float) Farstar.STAGE_WIDTH/2,(float) Farstar.STAGE_HEIGHT/2,0);
    }

    protected void setTableMenu(TableStage tableStage) {
        this.tableStage = tableStage;
        this.tableStage.setViewport(viewport);
        if (!game.getInputMultiplexer().getProcessors().contains(tableStage, true)) {
            game.getInputMultiplexer().addProcessor(tableStage);
        }
    }

    protected void drawContent(float delta, Batch batch) { }//for all screens except intro

    protected void drawMenus(float delta) { }//for all screens except intro

    protected void drawSigns(float delta, Batch batch) { notificationManager.drawAll(delta, batch, shapeRenderer); }

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
            tableStage.drawBackground(game.batch);
            tableStage.act(delta);
            tableStage.draw();
        }

        drawMenus(delta);

        game.batch.begin();
        game.batch.setColor(1, 1, 1, 1);
        drawContent(delta, game.batch);
        drawSigns(delta, game.batch);
        game.batch.end();

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

}
