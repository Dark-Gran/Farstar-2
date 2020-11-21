package com.darkgran.farstar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

public class IntroScreen extends SuperScreen {
    private final Texture logo;
    private float alpha = 0;
    private boolean fadeDirection = true; //true in, false out

    public IntroScreen(final Farstar game) {
        super(game);
        Gdx.input.setCursorCatched(true);
        logo = new Texture("DGLogo.jpg");
        try { Thread.sleep(500); } catch(InterruptedException ignored) { }
    }

    private void moveOn() {
        exit = new Texture("exit.png"); //TODO:
        game.setScreen(new MenuScreen(game, exit));
    }

    @Override
    public void render(float delta) {
        if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)){ moveOn(); }

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();

        if (alpha < 0 && !fadeDirection) { //end=>goNext
            try { Thread.sleep(900); } catch (InterruptedException ignored) { }
            moveOn();
        } else { //intro animation
            game.batch.setProjectionMatrix(camera.combined);
            game.batch.begin();
            game.batch.setColor(1, 1, 1, alpha);
            game.batch.draw(logo, (float) (Farstar.STAGE_WIDTH/2-logo.getWidth()/2), (float) (Farstar.STAGE_HEIGHT/2-logo.getHeight()/2));
            game.batch.end();

            if (delta > 0.03f) { delta = 0.03f; } //todo: rework (like time-steps)?
            if (alpha >= 1) {
                fadeDirection = false;
                try { Thread.sleep(900); } catch(InterruptedException ignored) { }
            }
            final float introSpeed = 0.35f;
            alpha += fadeDirection ? (introSpeed *delta) : -(introSpeed *delta)*4;
        }
    }

    @Override
    public void dispose() {
        logo.dispose();
    }

}