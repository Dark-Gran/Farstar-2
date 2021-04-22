package com.darkgran.farstar;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.darkgran.farstar.util.JustFont;
import com.darkgran.farstar.util.SimpleVector2;
import com.darkgran.farstar.util.TextDrawer;

public class Notification extends SimpleVector2 implements TextDrawer, JustFont {
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
        setX(notificationType.x);
        setY(notificationType.y);
        setFontColor(ColorPalette.MAIN);
        this.message = message;
        this.notificationType = notificationType;
    }

    @Override
    public void draw(Batch batch) {
        draw(getFont(), batch, getX(), getY(), message, fontColor);
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
