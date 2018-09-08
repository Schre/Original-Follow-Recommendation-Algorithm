package server.Messenger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Session {
    private MessageQueue messageQueue = new MessageQueue();
    // uid, user
    private Map<String, UserDTO> activeUsers = new HashMap<>();
    private final String sessionID = UUID.randomUUID().toString();

    public void userJoinedSession(UserDTO user) {
        activeUsers.putIfAbsent(user.getUid(), user);
    }

    public void userLeftSession(UserDTO user) {
        activeUsers.remove(user.getUid());
    }

    public String getSessionID() {
        return sessionID;
    }

    public void emitMessage(UserDTO sender, String data) {
        Message message = new Message(data, sender.getUid());
        messageQueue.pushMessage(message);
    }

    public Map<UserDTO, Message> ListMessagesForSession() {
        Map<UserDTO, Message> messages = new HashMap<>();
        List<Message> messageList = messageQueue.getMessages();

        for (int i = 0; i < messageList.size(); ++i) {
            Message message = messageList.get(i);
            messages.put(activeUsers.get(message.getUid()), message);
        }

        return messages;
    }
}
