package server.service;

import org.json.JSONObject;
import server.dto.dto.UserDTO;
import server.restapi.databaserestservice.UserRestService;

import javax.ws.rs.core.Response;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

public class UserService {
    /* Query and return user's data transfer object */
    public UserDTO getUser(String uid) {
        UserRestService urs = new UserRestService();
        Response res = urs.getUser(uid);

        if (res == null) {
            throw new NoSuchElementException("Error finding user with user_id=" + uid);
        }

        JSONObject json = new JSONObject( new JSONObject(res).get("entity").toString() );
        json = json.getJSONObject("obj0");

        UserDTO userDTO = new UserDTO();

        userDTO.user_id = uid;
        userDTO.email = json.getString("email");

        if (json.has("birth_date"))
            userDTO.birth_date = json.getString("birth_date");
        userDTO.username = json.getString("username");
        userDTO.password = json.getString("password");
        userDTO.first_name = json.getString("first_name");
        userDTO.last_name = json.getString("last_name");
        userDTO.gender = json.getString("gender").charAt(0);

        if (json.has("profile_picture_url"))
            userDTO.profile_picture_url = json.getString("profile_picture_url");

        userDTO.date_created = json.getString("date_created");
        userDTO.field = json.getString("field");

        return userDTO;
    }

    public Set<UserDTO> getUserFollowings(String uid) {
        Set<UserDTO> followers = new HashSet<>();
        UserRestService urs = new UserRestService();
        Response res = urs.getUserFollowings(uid);

        if (res == null) {
            throw new NoSuchElementException("Error finding user with user_id=" + uid);
        }

        JSONObject objects = new JSONObject(new JSONObject(res).get("entity").toString());
        for (String key : objects.keySet()) {
            try {
                JSONObject json = objects.getJSONObject(key);

                UserDTO userDTO = new UserDTO();

                userDTO.user_id = json.getString("user_id");
                userDTO.email = json.getString("email");

                if (json.has("birth_date"))
                    userDTO.birth_date = json.getString("birth_date");
                userDTO.username = json.getString("username");
                userDTO.password = json.getString("password");
                userDTO.first_name = json.getString("first_name");
                userDTO.last_name = json.getString("last_name");
                userDTO.gender = json.getString("gender").charAt(0);

                if (json.has("profile_picture_url"))
                    userDTO.profile_picture_url = json.getString("profile_picture_url");
                userDTO.date_created = json.getString("date_created");
                userDTO.field = json.getString("field");

                followers.add(userDTO);
            }
            catch (Exception e) {
                continue;
            }
        }
        return followers;
    }
}
