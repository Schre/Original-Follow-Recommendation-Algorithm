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
import java.util.*;

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

    public List<PostDTO> getKMostRecentPosts(String user_id, int K, boolean includeContent) {
        List<PostDTO> posts = this.getPosts(user_id, includeContent);
        Collections.sort(posts);
        K = Math.min(K, posts.size());
        return posts.subList(0, K);
    }

    public PostDTO getPost(String post_id, boolean includeContent) {

        JSONObject json;

        try {
            QueryBuilder qb = new QueryBuilder().select()
                    .star().from().literal("Posts ")
                    .where()
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
                ret.content = (new FileReader()).readFile(ret.user_id, ret.type, post_id);
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

    public boolean likePost(String user_id, String post_id) {
        try {
            String date_created = Long.toString(new java.util.Date().getTime());
            QueryBuilder qb = new QueryBuilder().insert()
                    .into().literal("Likes ")
                    .literal("(")
                    .literal("post_id, user_id, date_created) ")
                    .literal(" VALUES(")
                    .commaSeparatedStrings(Arrays.asList(new String[]{
                            post_id, user_id, date_created}))
                    .literal(") ");

            if (!QueryExecutor.execute(qb.build())) {
                return false;
            }
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean userLikesPost(String user_id, String post_id) {
        try {
            QueryBuilder qb = new QueryBuilder().select()
                    .literal("count(*) as count")
                    .from()
                    .literal("Likes ")
                    .where()
                    .literal("post_id=")
                    .string(post_id)
                    .and()
                    .literal("user_id=")
                    .string(user_id);

            JSONObject json = QueryExecutor.runQuery(qb.build());
            return Integer.parseInt(json.getJSONObject("obj0").getString("count")) == 1;
        }
        catch (Exception e) {
            return false;
        }
    }
    public boolean unlikePost(String user_id, String post_id) {
        try {
            QueryBuilder qb = new QueryBuilder().delete()
                    .from().literal("Likes ")
                    .where()
                    .literal("user_id=")
                    .string(user_id)
                    .and()
                    .literal("post_id=")
                    .string(post_id);

            if (!QueryExecutor.execute(qb.build())) {
                return false;
            }
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }

    public int countLikes(String post_id) {
        try {
            String date_created = Long.toString(new java.util.Date().getTime());
            QueryBuilder qb = new QueryBuilder().select()
                    .literal("count(*) as count")
                    .from()
                    .literal("Likes ")
                    .where()
                    .literal("post_id=")
                    .string(post_id);

            JSONObject json = QueryExecutor.runQuery(qb.build());
            return Integer.parseInt(json.getJSONObject("obj0").getString("count"));
        }
        catch (Exception e) {
            return 0;
        }
    }
}
