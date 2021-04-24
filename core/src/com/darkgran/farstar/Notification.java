package com.darkgran.farstar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.darkgran.farstar.util.*;

/**
 *  Stylized on-screen message.
 *  Uses NotificationType for placement.
 */
public class Notification extends TextInTheBox {
    public enum NotificationType {
        BOT_LEFT(Farstar.STAGE_WIDTH/10f, Farstar.STAGE_HEIGHT/5f);

        private final float x;
        private final float y;

        NotificationType(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

    private static final int MIN_DURATION = 3;
    private final NotificationType notificationType;
    private final DeltaCounter timer;

    /** @param duration Time in seconds. Set to MIN_DURATION unless greater duration is provided. */
    protected Notification(NotificationType notificationType, String message, int duration) {
        super(ColorPalette.LIGHT, ColorPalette.changeAlpha(ColorPalette.DARK, 0.5f), "fonts/barlow30.fnt", message);
        this.notificationType = notificationType;
        SimpleVector2 textWH = TextDrawer.getTextWH(getFont(), message);
        setupBox(notificationType.x, notificationType.y, textWH.getX(), textWH.getY());
        timer = new DeltaCounter(true, Math.max(duration, MIN_DURATION), 0);
    }

    protected void update(float delta) {
        timer.update(delta);
    }

    @Override
    public void draw(Batch batch, ShapeRenderer shapeRenderer) {
        float a = timeToAlpha(timer.getCount(), timer.getCountCap());
        batch.end();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(ColorPalette.changeAlpha(getBoxColor(), a));
        shapeRenderer.rect(notificationType.x - Farstar.STAGE_WIDTH / 19f, notificationType.y + getSimpleBox().getHeight() / 2, Farstar.STAGE_WIDTH / 3f, -getSimpleBox().getHeight() * 2.1f);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        batch.begin();
        drawText(getFont(), batch, notificationType.x, notificationType.y, getText(), ColorPalette.changeAlpha(getFontColor(), a));
    }

    private float timeToAlpha(float time, float duration) {
        if (time < 1) {
            return timer.getAccumulator();
        } else if (time > duration-2) {
            return 1-timer.getAccumulator();
        }
        return 1;
    }

    public SimpleCounter getTimer() {
        return timer;
    }
}
