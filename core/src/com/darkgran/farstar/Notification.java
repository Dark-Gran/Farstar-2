package com.darkgran.farstar;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.darkgran.farstar.util.JustFont;
import com.darkgran.farstar.util.TextDrawer;

/**
 *  Stylized on-screen message.
 *  Uses NotificationType for placement.
 */
public class Notification implements TextDrawer, JustFont { //TODO
    public enum NotificationType {
        BOT_LEFT(Farstar.STAGE_WIDTH/10f, Farstar.STAGE_HEIGHT/5f);

        private final float x;
        private final float y;

        NotificationType(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

    private String fontPath = "";
    private Color fontColor = new Color();
    private String message = "";
    private final NotificationType notificationType;
    private final GlyphLayout layout = new GlyphLayout();

    public Notification(NotificationType notificationType, String message) {
        setFont("fonts/barlow24.fnt");
        setFontColor(ColorPalette.MAIN);
        this.message = message;
        this.notificationType = notificationType;
        layout.setText(getFont(), message);
    }

    public void draw(Batch batch, ShapeRenderer shapeRenderer) {
        batch.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.rect(notificationType.x, notificationType.y, layout.width, -layout.height);
        shapeRenderer.end();
        batch.begin();
        draw(batch);
    }

    @Override
    public void draw(Batch batch) {
        draw(getFont(), batch, notificationType.x, notificationType.y, message, fontColor);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NotificationType getNotificationType() {
        return notificationType;
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
}
