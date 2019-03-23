package server.service;

import server.notifications.ActionNotification;
import server.shared.SharedObjects;

import java.util.List;

public class NotificationService {
    public List<ActionNotification> getNotificationsForUser(String user_id) {
        return SharedObjects.getActionNotificationManager().getNotificationsForUser(user_id);
    }

    public void addActionNotificationForUser(String user_id, ActionNotification notification) {
        SharedObjects.getActionNotificationManager().addNotificationForUser(user_id, notification);
    }

    public void deleteActionNotificationForUser(String user_id, ActionNotification notification) {
        SharedObjects.getActionNotificationManager().addNotificationForUser(user_id, notification);
    }
}
