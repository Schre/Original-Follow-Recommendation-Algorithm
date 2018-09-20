package server.dto.Serializers;


import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import server.dto.dto.CostObjectDTO;

import java.io.IOException;

public class CostObjectSerializer {
    private ObjectMapper objectMapper;

    public CostObjectSerializer() throws Exception {
        objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    // TODO: Implement this
    public String serialize(CostObjectDTO co) throws Exception {
        String ret = "";
        try {
            ret = objectMapper.writeValueAsString(co);
        } catch (JsonProcessingException e) {
            System.err.println("JSON Processing exception while serializing userdto" +
                    ": " + e.getMessage());
            throw e;
        }
        return ret;
    }

    public CostObjectDTO deserialize(String json) throws Exception {
        CostObjectDTO ret = null;
        Exception ex = null;
        try {
            ret = objectMapper.readValue(json, CostObjectDTO.class);
        } catch (JsonParseException e) {
            System.err.println("JSON Parsing exception while deserializing cost object" +
                    ": " + e.getMessage());
            ex = e;
        } catch (JsonMappingException e) {
            System.err.println("JSON Mapping exception while deserializing cost object" +
                    ": " + e.getMessage());
            ex = e;
        } catch (IOException e) {
            System.err.println("IO Exception exception while deserializing cost object" +
                    ": " + e.getMessage());
            ex = e;
        }

        if (ex != null) {
            throw ex;
        }

        return ret;
    }
}
