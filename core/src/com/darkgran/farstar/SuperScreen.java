package com.darkgran.farstar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class SuperScreen implements Screen {
    final Farstar game;
    public final OrthographicCamera camera;
    public final Viewport viewport;
    public Texture exit;

    public SuperScreen(final Farstar game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Farstar.STAGE_WIDTH, Farstar.STAGE_HEIGHT);
        viewport = new ExtendViewport(Farstar.STAGE_WIDTH, Farstar.STAGE_HEIGHT, camera);
        viewport.apply();
        camera.position.set((float) Farstar.STAGE_WIDTH/2,(float) Farstar.STAGE_HEIGHT/2,0);
    }

    public void control() { }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        drawScreen();
        drawTable();
        control();
    }

    public void drawScreen() { }//for all screens except intro

    public void drawTable() { //for all screens except intro
        if (exit != null) {
            game.batch.begin();
            game.batch.setColor(1, 1, 1, 1);

            game.batch.draw(exit, 1, 1, (float) Farstar.STAGE_WIDTH/40,(float) Farstar.STAGE_HEIGHT/20);

            game.batch.end();
        }
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
