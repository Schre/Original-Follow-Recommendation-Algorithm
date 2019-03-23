package server.notifications;

import java.util.*;

public class ActionNotificationManager {
    public HashMap<String, LinkedHashMap<String, ActionNotification>> notifications;

    public ActionNotificationManager() {
        notifications = new HashMap<>();
    }

    public void addNotificationForUser(String user_id, ActionNotification notification) {
        notifications.putIfAbsent(user_id, new LinkedHashMap<String, ActionNotification>());
        LinkedHashMap<String, ActionNotification> map = notifications.get(user_id);
        map.put(notification.notification_id, notification);
    }

    public List<ActionNotification> getNotificationsForUser(String user_id) {
        List<ActionNotification> ret = new ArrayList<>();

        // make sure most recent first!
        for (ActionNotification notification : notifications.get(user_id).values()) {
            ret.add(0, notification);
        }

        return ret;
    }

    public boolean deleteNotification(String user_id, ActionNotification notification) {
        if (!notifications.containsKey(user_id)) {
            return false;
        }

        return (notifications.get(user_id).remove(notification.notification_id) != null);
    }

}
