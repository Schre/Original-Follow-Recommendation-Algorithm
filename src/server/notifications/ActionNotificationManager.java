package server.notifications;

import java.util.*;

public class ActionNotificationManager {
    private Map<String, LinkedHashMap<String, ActionNotification>> notifications;
    private Map<String, Set<String>> uidToPostLikeMapping;

    public ActionNotificationManager() {
        notifications = new HashMap<>();
        uidToPostLikeMapping = new HashMap<>();
    }

    public void addNotification(ActionNotification notification) {
        String user_id = notification.forUserId;

        if (notification instanceof LikeNotification) {
            String likedBy = ((LikeNotification) notification).getInitiatedByUserId();
            String postLiked = ((LikeNotification) notification).getForPostId();
            if (uidToPostLikeMapping.containsKey(likedBy)
                && uidToPostLikeMapping.get(likedBy).contains(postLiked)) {
                    return; // already liked it
            }
            else {
                uidToPostLikeMapping.putIfAbsent(likedBy, new HashSet<>());
                uidToPostLikeMapping.get(likedBy).add(postLiked);
            }
        }

        notifications.putIfAbsent(user_id, new LinkedHashMap<String, ActionNotification>());
        LinkedHashMap<String, ActionNotification> map = notifications.get(user_id);
        map.put(notification.getNotificationId(), notification);
    }

    public List<ActionNotification> getNotificationsForUser(String user_id) {
        List<ActionNotification> ret = new ArrayList<>();

        if (!notifications.containsKey(user_id)) {
            return ret;
        }
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

        return (notifications.get(user_id).remove(notification.getForUserId()) != null);
    }

}
