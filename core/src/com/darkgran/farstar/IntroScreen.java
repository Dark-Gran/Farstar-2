package com.darkgran.farstar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

public class IntroScreen extends SuperScreen { //Animation used only once on app-launch
    private final Texture logo = new Texture("images/logo.jpg");
    private float alpha = 0;
    private boolean fadeDirection = true; //true in, false out

    private final static float INTRO_SPEED = 0.35f;

    public IntroScreen(final Farstar game) {
        super(game);
        Gdx.input.setCursorCatched(true);
        try { Thread.sleep(500); } catch(InterruptedException ignored) { }
    }

    private void endIntro() {
        Gdx.input.setInputProcessor(getGame().getInputMultiplexer());
        this.dispose();
        getGame().setScreen(new MenuScreen(getGame(), new TableMenu(getGame(), getViewport())));
    }

    private void updateAlpha(float delta) { //rework? (like time-steps)?
        if (delta > 0.03f) { delta = 0.03f; }
        if (alpha >= 1) {
            fadeDirection = false;
            try { Thread.sleep(900); } catch(InterruptedException ignored) { }
        }
        alpha += fadeDirection ? (INTRO_SPEED *delta) : -(INTRO_SPEED *delta)*4;
    }

    @Override
    public void render(float delta) {

        //control
        if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)){ endIntro(); }

        //INTRO ANIMATION
        if (alpha < 0 && !fadeDirection) { //animation over
            try { Thread.sleep(900); } catch (InterruptedException ignored) { }
            endIntro();
        } else {

            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            getCamera().update();
            getGame().batch.setProjectionMatrix(getCamera().combined);
            getGame().batch.begin();
            getGame().batch.setColor(1, 1, 1, alpha);

            getGame().batch.draw(logo, (float) (Farstar.STAGE_WIDTH/2-logo.getWidth()/2), (float) (Farstar.STAGE_HEIGHT/2-logo.getHeight()/2));

            getGame().batch.end();

            updateAlpha(delta);

        }
    }

    @Override
    public void dispose() {
        logo.dispose();
    }

}