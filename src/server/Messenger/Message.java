package server.Messenger;

public class Message {
    private final String data;
    private final String uid;

    public Message(String data, String uid) {
        this.data = data;
        this.uid = uid;
    }

    public String getMessage() {
        return data;
    }

    public String getUid() {
        return uid;
    }
}
