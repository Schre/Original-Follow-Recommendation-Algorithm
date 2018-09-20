package server.dto.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDTO {
    public String username;
    public String passwordHash;
    public String firstName;
    public String lastName;
    public String user_id;
    public int permissionLevel;
}
