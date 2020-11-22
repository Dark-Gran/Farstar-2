package com.darkgran.farstar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.darkgran.farstar.menus.TableMenu;

public abstract class SuperScreen implements Screen {
    private Farstar game;
    private OrthographicCamera camera = new OrthographicCamera();
    private Viewport viewport = new ExtendViewport(Farstar.STAGE_WIDTH, Farstar.STAGE_HEIGHT, camera);
    private TableMenu tableMenu;

    public SuperScreen(final Farstar game) {
        this.game = game;
        camera.setToOrtho(false, Farstar.STAGE_WIDTH, Farstar.STAGE_HEIGHT);
        viewport.apply();
        camera.position.set((float) Farstar.STAGE_WIDTH/2,(float) Farstar.STAGE_HEIGHT/2,0);
    }

    public void setTableMenu(TableMenu tableMenu) {
        this.tableMenu = tableMenu;
        if (!game.getInputMultiplexer().getProcessors().contains(tableMenu, true)) {
            game.getInputMultiplexer().addProcessor(tableMenu);
        }
    }

    public void drawScreen(float delta, Batch batch) { }//for all screens except intro

    public Farstar getGame() { return game; }

    public OrthographicCamera getCamera() { return camera; }

    public Viewport getViewport() { return viewport; }

    public TableMenu getTableMenu() {
        return tableMenu;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        if (tableMenu != null) {
            tableMenu.act(Gdx.graphics.getDeltaTime());
            tableMenu.draw();
        }

        getGame().batch.begin();
        getGame().batch.setColor(1, 1, 1, 1);
        drawScreen(delta, getGame().batch);
        getGame().batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set((float) Farstar.STAGE_WIDTH/2,(float) Farstar.STAGE_HEIGHT/2,0);
        camera.update();
    }

    @Override
    public void show() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
