package com.darkgran.farstar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.darkgran.farstar.ui.TableMenu;

public class SuperScreen implements Screen {
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

    public Farstar getGame() { return game; }

    public OrthographicCamera getCamera() { return camera; }

    public Viewport getViewport() { return viewport; }

    public void setTableMenu(TableMenu tableMenu) {
        this.tableMenu = tableMenu;
        game.getInputMultiplexer().addProcessor(tableMenu);
    }

    public TableMenu getTableMenu() {
        return tableMenu;
    }

    public void drawScreen(float delta) { }//for all screens except intro

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
        drawScreen(delta);
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
