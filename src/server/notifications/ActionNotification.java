package server.notifications;

import org.json.JSONObject;

import java.util.UUID;

// TODO: This will have to be used with Jackson in a way to support polymorphism since not all notifications will have the same attributes
public abstract class ActionNotification {
    protected long timestamp;
    protected String forUserId;
    protected String type;
    protected String notificationId;
    protected String message;
    protected String initiatedByUserId;

    public ActionNotification(String forUserId) {
        this.forUserId = forUserId;
        this.timestamp = new java.util.Date().getTime();
        this.notificationId = UUID.randomUUID().toString();
    }

    public String getForUserId() {
        return this.forUserId;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public String getMessage() {
        return this.message;
    }

    public String getInitiatedByUserId(){
        return this.initiatedByUserId;
    }

    public JSONObject serialize() {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("timestamp", timestamp);
        jsonObject.put("forUserId", forUserId);
        jsonObject.put("type", type);
        jsonObject.put("notificationId", notificationId);
        jsonObject.put("message", message);
        jsonObject.put("initiatedByUserId", initiatedByUserId);
        return jsonObject;
    }

    public String getNotificationId() {
        return notificationId;
    }
}
