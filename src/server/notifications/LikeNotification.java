package server.notifications;

import org.json.JSONObject;
import server.dto.dto.UserDTO;
import server.service.UserService;

public class LikeNotification extends ActionNotification{
    private String forPostId;

    public LikeNotification(String fromUserId, String likedByUserId, String forPostId) {
        super(fromUserId);
        this.initiatedByUserId = likedByUserId;
        this.type = "LikeNotification";
        this.forPostId = forPostId;
        UserDTO likedBy = (new UserService()).getUser(likedByUserId);
        this.message = likedBy.first_name + " " + likedBy.last_name + " liked your post!";
    }


    public String getForPostId() {
        return forPostId;
    }

    @Override
    public JSONObject serialize(){
        JSONObject ret = super.serialize();
        return ret;
    }
}
