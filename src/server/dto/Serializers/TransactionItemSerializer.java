package server.dto.Serializers;


import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import server.dto.dto.TransactionalItemDTO;

import java.io.IOException;

public class TransactionItemSerializer {
    private ObjectMapper objectMapper;

    public TransactionItemSerializer() throws Exception {
        objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public String serialize(TransactionalItemDTO co) throws Exception {
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

    public TransactionalItemDTO deserialize(String json) throws Exception {
        TransactionalItemDTO ret = null;
        Exception ex = null;
        try {
            ret = objectMapper.readValue(json, TransactionalItemDTO.class);
        } catch (JsonParseException e) {
            System.err.println("JSON Parsing exception while deserializing TransactionalItemDTO" +
                    ": " + e.getMessage());
            ex = e;
        } catch (JsonMappingException e) {
            System.err.println("JSON Mapping exception while deserializing TransactionalItemDTO" +
                    ": " + e.getMessage());
            ex = e;
        } catch (IOException e) {
            System.err.println("IO Exception exception while deserializing TransactionalItemDTO" +
                    ": " + e.getMessage());
            ex = e;
        }

        if (ex != null) {
            throw ex;
        }

        return ret;
    }
}
