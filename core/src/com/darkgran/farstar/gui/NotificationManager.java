package com.darkgran.farstar.gui;

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

    public void drawAll(Batch batch, ShapeRenderer shapeRenderer) {
        if (notifications.size() > 0) {
            for (Map.Entry<Notification.NotificationType, ArrayList<Notification>> entry : notifications.entrySet()) {
                if (entry.getValue().size() > 0) {
                    entry.getValue().get(0).draw(batch, shapeRenderer);
                }
            }
        }
    }

    public void update(float delta) {
        for (Map.Entry<Notification.NotificationType, ArrayList<Notification>> entry : notifications.entrySet()) {
            if (entry.getValue().size() > 0) {
                entry.getValue().get(0).update(delta);
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
                if (notification.getText().equals(message)) {
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

    public void clear(Notification.NotificationType notificationType) {
        notifications.remove(notificationType);
    }

    public void clearAll() {
        notifications.clear();
    }

}