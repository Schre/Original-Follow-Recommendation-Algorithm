package server.Messenger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MessageQueue {
    private ConcurrentLinkedQueue<Message> messageQueue;

    public void pushMessage(Message msg) {
        messageQueue.add(msg);
    }

    public Message getNextMessageToProcess() {
        return messageQueue.remove();
    }

    public List<Message> getMessages() {
        Object[] messages = messageQueue.toArray();
        List<Message> ret = new ArrayList<>();

        for (Object obj : messages) {
            Message msg = (Message) obj;
            ret.add(msg);
        }

        return ret;
    }

}
