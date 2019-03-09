package server.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import server.database.queryengine.QueryBuilder;
import server.database.queryengine.QueryExecutor;
import server.dto.dto.PostDTO;
import server.etc.Constants;
import server.filesystem.FileReader;
import server.filesystem.FileWriter;
import server.restapi.databaserestservice.PostRestService;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

public class PostService {

    public boolean createPost(PostDTO post) {
        try {
            QueryBuilder qb = new QueryBuilder().insert()
                    .into().literal("Posts")
                    .literal("(")
                    .literal("post_id, user_id, category, type, post_url, date_created) ")
                    .literal(" VALUES(")
                    .commaSeparatedStrings(Arrays.asList(new String[]{
                            post.post_id, post.user_id, post.category, post.type, post.post_url, post.date_created}))
                    .literal(") ");

            if (!QueryExecutor.execute(qb.build())) {
                return false;
            }

            FileWriter fw = new FileWriter();
            fw.writeFile(post.user_id, post.post_id, "text", post.content);
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }
    public PostDTO getPost(String user_id, String post_id, boolean includeContent) {

        JSONObject json;

        try {
            QueryBuilder qb = new QueryBuilder().select()
                    .star().from().literal("Posts ")
                    .where()
                    .literal("user_id=")
                    .string(user_id)
                    .literal(" ")
                    .and()
                    .literal("post_id=")
                    .string(post_id);
            json = QueryExecutor.runQuery(qb.build()).getJSONObject("obj0");
        }
        catch (Exception e) {
            System.out.print(e.getMessage());
            return null;
        }


        ObjectMapper objectMapper = new ObjectMapper();

        try {
            PostDTO ret = objectMapper.readValue(json.toString(Constants.JSON_INDENT_FACTOR), PostDTO.class);
            if (includeContent) {
                ret.content = (new FileReader()).readFile(user_id, ret.type, post_id);
            }
            return ret;
        }
        catch (JsonParseException jpe) {
            jpe.printStackTrace();
            return null;
        }
        catch (JsonMappingException e) {
            e.printStackTrace();
            return null;
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<PostDTO> getPosts(String user_id, boolean includeContent) {
        JSONObject json;

        try {
            QueryBuilder qb = new QueryBuilder().select()
                    .star().from().literal("Posts ")
                    .where()
                    .literal("user_id=")
                    .string(user_id);
            json = QueryExecutor.runQuery(qb.build());
        }
        catch (Exception e) {
            System.out.print(e.getMessage());
            return null;
        }


        ObjectMapper objectMapper = new ObjectMapper();

        try {
            List<PostDTO> ret = new ArrayList<>();
            for (String key : json.keySet()) {
                JSONObject jsonObject = json.getJSONObject(key);
                PostDTO obj = objectMapper.readValue(jsonObject.toString(Constants.JSON_INDENT_FACTOR), PostDTO.class);
                // fetch content from filesystem
                if (includeContent) {
                    obj.content = (new FileReader()).readFile(user_id, obj.type, obj.post_id);
                }
                ret.add(obj);
            }
            return ret;
        }
        catch (JsonParseException jpe) {
            jpe.printStackTrace();
            return null;
        }
        catch (JsonMappingException e) {
            e.printStackTrace();
            return null;
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
