package com.darkgran.farstar;

import com.badlogic.gdx.graphics.g2d.Batch;

import java.util.ArrayList;

/**
 * Controls all notifications across all Screens.
 */
public class NotificationManager {
    private ArrayList<Notification> notifications = new ArrayList<>();

    public void drawAll(float delta, Batch batch) {
        for (Notification notification : notifications) {
            notification.draw(batch);
        }
    }

    public boolean newNotification(Notification.NotificationType notificationType, String message) {
        for (Notification notification : notifications) {
            if (notification.getNotificationType() == notificationType && notification.getMessage() == message) {
                return false;
            }
        }
        notifications.add(new Notification(notificationType, message));
        return true;
    }

}