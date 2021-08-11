package com.darkgran.farstar.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.darkgran.farstar.util.*;

import static com.darkgran.farstar.Farstar.STAGE_HEIGHT;
import static com.darkgran.farstar.Farstar.STAGE_WIDTH;

/**
 *  Stylized on-screen message.
 *  Uses NotificationType for placement.
 */
public class Notification extends TextInTheBox {
    public enum NotificationType {
        BOT_LEFT(STAGE_WIDTH/12f, STAGE_HEIGHT/5f, STAGE_WIDTH * 0.36f, 50f, -70f, "bahnschrift30"),
        MIDDLE(STAGE_WIDTH/2f, STAGE_HEIGHT/2f, STAGE_WIDTH * 0.2f, 80f, 0f, "orbitron36");

        private final float x;
        private final float y;
        private final float boxWidth;
        private final float boxHeight;
        private final float boxOffsetX; //overwrites centralization unless set to zero
        private final String fontName;

        NotificationType(float x, float y, float boxWidth, float boxHeight, float boxOffsetX, String fontName) {
            this.x = x;
            this.y = y;
            this.boxWidth = boxWidth;
            this.boxHeight = boxHeight;
            this.boxOffsetX = boxOffsetX;
            this.fontName = fontName;
        }
    }

    private final NotificationType notificationType;
    private final DeltaCounter timer;

    /** @param duration Time in seconds. Set to MIN_DURATION unless greater duration is provided. */
    protected Notification(NotificationType notificationType, String message, int duration) {
        super(ColorPalette.LIGHT, ColorPalette.changeAlpha(ColorPalette.DARK, 0.5f), "fonts/"+notificationType.fontName+".fnt", message, notificationType.x, notificationType.y, notificationType.boxWidth, notificationType.boxHeight);
        this.notificationType = notificationType;
        if (notificationType.boxOffsetX != 0f) {
            getSimpleBox().x = notificationType.x+notificationType.boxOffsetX;
        }
        if (notificationType == NotificationType.MIDDLE) {
            SimpleVector2 textWH = TextDrawer.getTextWH(getFont(), message);
            this.x = this.x-textWH.x/2f;
            this.y = this.y+textWH.y;
            centralizeBox();
        }
        int MIN_DURATION = 3;
        timer = new DeltaCounter(true, Math.max(duration, MIN_DURATION), 0);
    }

    protected void update(float delta) {
        timer.update(delta);
    }

    @Override
    public void draw(Batch batch, ShapeRenderer shapeRenderer) {
        float a = timeToAlpha(timer.getAccumulator(), timer.getCountCap());
        batch.end();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(ColorPalette.changeAlpha(getBoxColor(), a));
        shapeRenderer.rect(getSimpleBox().x, getSimpleBox().y, getSimpleBox().getWidth(), getSimpleBox().getHeight());
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        batch.begin();
        drawText(getFont(), batch, this.x, this.y, getText(), ColorPalette.changeAlpha(getFontColor(), a));
    }

    private float timeToAlpha(float time, float duration) {
        if (time < 1) {
            return timer.getAccumulator();
        } else if (time > duration-1f) {
            return duration-timer.getAccumulator();
        }
        return 1;
    }

    public DeltaCounter getTimer() {
        return timer;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }
}
