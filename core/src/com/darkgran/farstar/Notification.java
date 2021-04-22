package com.darkgran.farstar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.darkgran.farstar.util.JustFont;
import com.darkgran.farstar.util.SimpleCounter;
import com.darkgran.farstar.util.TextDrawer;

/**
 *  Stylized on-screen message.
 *  Uses NotificationType for placement.
 */
public class Notification implements TextDrawer, JustFont {
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
    private String fontPath = "";
    private Color fontColor = new Color();
    private String message = "";
    private final NotificationType notificationType;
    private final GlyphLayout layout = new GlyphLayout();
    private final Color boxColor = new Color(0f, 0f, 0f, 1f);
    private final SimpleCounter timer;

    protected Notification(NotificationType notificationType, String message, int duration) {
        setFont("fonts/barlow24.fnt");
        setFontColor(ColorPalette.MAIN);
        this.message = message;
        this.notificationType = notificationType;
        layout.setText(getFont(), message);
        timer = new SimpleCounter(true, Math.max(duration, MIN_DURATION), 0);
    }

    protected void draw(Batch batch, ShapeRenderer shapeRenderer) {
        batch.end();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(boxColor);
        shapeRenderer.rect(notificationType.x-Farstar.STAGE_WIDTH/19f, notificationType.y+layout.height/2, layout.width+Farstar.STAGE_WIDTH/5f, -layout.height*2.1f);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        batch.begin();
        draw(batch);
    }

    @Override
    public void draw(Batch batch) {
        draw(getFont(), batch, notificationType.x, notificationType.y, message, fontColor);
    }

    protected String getMessage() {
        return message;
    }

    @Override
    public String getFontPath() {
        return fontPath;
    }

    @Override
    public void setFontPath(String path) {
        fontPath = path;
    }

    @Override
    public Color getFontColor() {
        return fontColor;
    }

    @Override
    public void setFontColor(Color fontColor) {
        this.fontColor = fontColor;
    }

    public SimpleCounter getTimer() {
        return timer;
    }
}
