package server.dto.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDTO {
    public String user_id;
    public String email;
    public String username;
    public String password;
    public String first_name;
    public String last_name;
    public Character gender;
    public String profile_picture_url;
    public String birth_date;
    public String date_created;
    public String field;
    public static String generateUserId() {
        return UUID.randomUUID().toString().substring(0, 20);
    }

}
