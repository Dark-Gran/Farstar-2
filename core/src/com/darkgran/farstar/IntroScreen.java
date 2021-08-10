package com.darkgran.farstar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.darkgran.farstar.gui.*;
import com.darkgran.farstar.mainscreen.MainScreen;
import com.darkgran.farstar.util.Delayer;

public class IntroScreen extends SuperScreen implements Delayer { //Animation used only once on app-launch
    private final SimpleImage2 logo;
    private boolean active = false;
    private float alpha = 0;
    private boolean fadeDirection = true; //true in, false out

    public IntroScreen(final Farstar game, ScreenSettings screenSettings) {
        super(game, screenSettings);
        hideCursor(true);
        delayAction(this::activate, 0.5f);
        TextureRegion ltr = AssetLibrary.getInstance().getAtlasRegion("logo");
        logo = new SimpleImage2((float) (Farstar.STAGE_WIDTH / 2 - ltr.getRegionWidth() / 2), (float) (Farstar.STAGE_HEIGHT / 2 - ltr.getRegionHeight() / 2), ltr);
    }

    @Override
    public void userEscape() {
        endIntro();
    }

    private void endIntro() {
        getGame().setScreen(new MainScreen(getGame(), getScreenSettings(), new TableStage(getGame(), getViewport())));
        NotificationManager.getInstance().newNotification(Notification.NotificationType.BOT_LEFT, "Alpha Warning: Most Features Not Available.", 8);
    }

    private void activate() { active = true; }

    private void updateAlpha(float delta) {
        //if (delta > 0.03f) { delta = 0.03f; }
        final float introSpeed = 0.35f;
        alpha += fadeDirection ? (introSpeed *delta) : -(introSpeed *delta)*4;
        if (alpha >= 1) {
            fadeDirection = false;
            active = false;
            delayAction(this::activate, 0.9f);
        }
    }

    @Override
    public void render(float delta) {
        //control
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            endIntro();
        }

        //INTRO ANIMATION

        if (active) {
            if (alpha < 0 && !fadeDirection) { //animation over
                active = false;
                fadeDirection = true;
                delayAction(this::endIntro, 0.5f);
            } else {
                updateAlpha(delta);
            }
        }

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        getCamera().update();
        getGame().batch.setProjectionMatrix(getCamera().combined);
        getGame().batch.begin();
        getGame().batch.setColor(1, 1, 1, (active || !fadeDirection) ? alpha : 0);

        logo.draw(getGame().batch);

        getGame().batch.setColor(1, 1, 1, 1);

        drawSigns(getGame().batch);

        getGame().batch.end();

        update(delta);

    }

}