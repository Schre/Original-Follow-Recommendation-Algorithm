package server.notifications;

// TODO: This will have to be used with Jackson in a way to support polymorphism since not all notifications will have the same attributes
public abstract class ActionNotification {
    public String notificationText;
    public long timestamp;
    public String user_id;
    public String notification_id;
}
