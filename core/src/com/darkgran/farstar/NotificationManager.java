package com.darkgran.farstar;

import com.badlogic.gdx.graphics.g2d.Batch;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

/**
 * Controls all notifications across all Screens.
 */
public class NotificationManager {
    private EnumMap<Notification.NotificationType, ArrayList<Notification>> notifications = new EnumMap<>(Notification.NotificationType.class);

    public void drawAll(float delta, Batch batch) {
        for (Map.Entry<Notification.NotificationType, ArrayList<Notification>> entry : notifications.entrySet()) {
            for (Notification notification : entry.getValue()) {
                notification.draw(batch);
            }
        }
    }

    public boolean newNotification(Notification.NotificationType notificationType, String message) {
        ArrayList<Notification> n = new ArrayList<>();
        //check for duplicates
        if (notifications.get(notificationType) != null) {
            for (Notification notification : notifications.get(notificationType)) {
                if (notification.getMessage() == message) {
                    return false;
                }
            }
            n = notifications.get(notificationType);
        }
        //add to list
        n.add(new Notification(notificationType, message));
        notifications.put(notificationType, n);
        return true;
    }

}