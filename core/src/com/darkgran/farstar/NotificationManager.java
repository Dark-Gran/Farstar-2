package com.darkgran.farstar;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

/**
 * Controls all notifications across all Screens.
 */
public class NotificationManager {
    private final EnumMap<Notification.NotificationType, ArrayList<Notification>> notifications = new EnumMap<>(Notification.NotificationType.class);

    public void drawAll(float delta, Batch batch, ShapeRenderer shapeRenderer) {
        for (Map.Entry<Notification.NotificationType, ArrayList<Notification>> entry : notifications.entrySet()) {
            if (entry.getValue().size() > 0) {
                entry.getValue().get(0).draw(batch, shapeRenderer);
            }
        }
    }

    public void update() {
        for (Map.Entry<Notification.NotificationType, ArrayList<Notification>> entry : notifications.entrySet()) {
            if (entry.getValue().size() > 0) {
                entry.getValue().get(0).getTimer().update();
                if (!entry.getValue().get(0).getTimer().isEnabled()) {
                    entry.getValue().remove(0);
                }
            }
        }
    }

    public boolean newNotification(Notification.NotificationType notificationType, String message, int duration) {
        ArrayList<Notification> n = new ArrayList<>();
        //check for duplicates
        if (notifications.get(notificationType) != null) {
            for (Notification notification : notifications.get(notificationType)) {
                if (notification.getMessage().equals(message)) {
                    return false;
                }
            }
            n = notifications.get(notificationType);
        }
        //add to list
        n.add(new Notification(notificationType, message, duration));
        notifications.put(notificationType, n);
        return true;
    }

}