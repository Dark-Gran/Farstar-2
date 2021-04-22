package com.darkgran.farstar;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.darkgran.farstar.util.JustFont;
import com.darkgran.farstar.util.TextDrawer;

/**
 *  Stylized on-screen message.
 *  Uses NotificationType for placement.
 */
public class Notification implements TextDrawer, JustFont {
    public enum NotificationType {
        BOT_LEFT(20, 20);

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

    public Notification(NotificationType notificationType, String message) {
        setFont("");
        setFontColor(ColorPalette.MAIN);
        this.message = message;
        this.notificationType = notificationType;
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
