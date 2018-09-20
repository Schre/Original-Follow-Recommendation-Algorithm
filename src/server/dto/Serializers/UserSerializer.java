package server.dto.Serializers;


import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import server.dto.dto.UserDTO;

import java.io.IOException;

public class UserSerializer {
    private ObjectMapper objectMapper;

    public UserSerializer() {
        objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    // TODO: Implement this
    public String serialize(UserDTO user) throws Exception {
        String ret = "";
        try {
            ret = objectMapper.writeValueAsString(user);
        } catch (JsonProcessingException e) {
            System.err.println("JSON Processing exception while serializing userdto" +
                    ": " + e.getMessage());
            throw e;
        }
        return ret;
    }

    public UserDTO deserialize(String json) throws Exception {
        UserDTO ret = null;
        Exception ex = null;
        try {
            ret = objectMapper.readValue(json, UserDTO.class);
        } catch (JsonParseException e) {
            System.err.println("JSON Parsing exception while deserializing userdto" +
                    ": " + e.getMessage());
            ex = e;
        } catch (JsonMappingException e) {
            System.err.println("JSON Mapping exception while deserializing userdto" +
                    ": " + e.getMessage());
            ex = e;
        } catch (IOException e) {
            System.err.println("IO Exception exception while deserializing userdto" +
                    ": " + e.getMessage());
            ex = e;
        }

        if (ex != null) {
            throw ex;
        }
        return ret;
    }
}
