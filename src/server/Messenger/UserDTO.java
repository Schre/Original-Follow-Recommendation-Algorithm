package server.Messenger;

import java.util.UUID;

/**
 * In the future this will be stored in a database and deserialized
 */
public class UserDTO {
    private final String uid;
    public final String displayName;

    public UserDTO(String displayName) {
        this.displayName = displayName;
        this.uid = UUID.randomUUID().toString();
    }

    public String getUid() {
        return uid;
    }
}
