package server.network;

import java.util.HashSet;
import java.util.Set;

/***
 * This class is utilized by the follwer recommendation system.
 */
public class NetworkNode {
    private Set<NetworkNode> follows;
    private String uid;
    private String field;

    public NetworkNode(String uid, String field) {
        this.uid = uid;
        follows = new HashSet<>();
        this.field = field;
    }

    public Set<NetworkNode> getUsersFollowed() {
        return follows;
    }

    public String getField() {
        return field;
    }

    public boolean addFollow(NetworkNode node) {
        return follows.add(node);
    }

    public boolean removeFollow(NetworkNode node) {
        return follows.remove(node);
    }

    public String getUID() {
        return uid;
    }
}
