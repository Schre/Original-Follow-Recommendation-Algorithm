package server.restapi.databaserestservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import server.database.queryengine.QueryBuilder;
import server.database.queryengine.QueryExecutor;
import server.dto.dto.PostDTO;
import server.dto.dto.UserDTO;
import server.etc.Constants;
import server.filesystem.FileReader;
import server.filesystem.FileWriter;
import server.restapi.RestService;
import server.service.UserService;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Path("post")
public class PostRestService extends RestService {

    @GET
    @Path("user/{uid}")
    public Response getPostsByUser(@PathParam("uid") String user_id) {
        JSONObject json = new JSONObject();

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
            return okJSON(Response.Status.OK, json.toString(Constants.JSON_INDENT_FACTOR));
        }
        return okJSON(Response.Status.OK, json.toString(Constants.JSON_INDENT_FACTOR));
    }

    @GET
    @Path("user/{uid}/feed")
    public Response getUserFeed(@PathParam("uid") String user_id) {
        UserService us = new UserService();
        JSONObject ret = new JSONObject();

        Set<UserDTO> followings = us.getUserFollowings(user_id);
        List<PostDTO> posts = new ArrayList<>();

        /* Get all posts by followers */
        for (UserDTO following : followings) {
            JSONObject postsJson = new JSONObject();
            try {
                QueryBuilder qb = new QueryBuilder().select()
                        .star().from().literal("Posts")
                        .where()
                        .literal("user_id=")
                        .string(following.user_id);
                postsJson = QueryExecutor.runQuery(qb.build());

                for (String postKey : postsJson.keySet()) {
                    JSONObject post = postsJson.getJSONObject(postKey);

                    ObjectMapper objectMapper = new ObjectMapper();
                    PostDTO postObj = objectMapper.readValue(post.toString(Constants.JSON_INDENT_FACTOR), PostDTO.class);
                    posts.add(postObj);
                }
            } catch (Exception e) {
                System.out.print(e.getMessage());
                return okJSON(Response.Status.OK, postsJson.toString(Constants.JSON_INDENT_FACTOR));
            }
        }


        Collections.sort(posts);

        int count = 0;
        for (PostDTO postDTO : posts) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                String s = objectMapper.writeValueAsString(postDTO);
                ret.put(Integer.toString(count), new JSONObject(s));
            }
            catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return okJSON(Response.Status.OK, ret.toString(Constants.JSON_INDENT_FACTOR));
    }

    @POST
    @Path("user/{uid}/type/{type}/file/{fid}")
    public Response writeToFile(@PathParam("uid") String user_id, @PathParam("type") String type, @PathParam("fid") String file_id, String content) {
        FileWriter writer = new FileWriter();
        JSONObject json = new JSONObject();

        json.put("posted", writer.writeFile(user_id, file_id, type, content));
        return okJSON(Response.Status.OK, json.toString(Constants.JSON_INDENT_FACTOR));
    }

    @POST
    @Path("user/{uid}")
    public Response createUserPost(@PathParam("uid") String user_id, String content) {
        JSONObject json = new JSONObject();
        java.sql.Date sqlDate = new java.sql.Date(new java.util.Date().getTime());
        String pid = PostDTO.generateId();
        try {
            PostDTO post = (new ObjectMapper()).readValue(content, PostDTO.class);
            post.post_id = pid;

            QueryBuilder qb = new QueryBuilder().insert()
                    .into().literal("Posts")
                    .literal("(")
                    .literal("post_id, user_id, category, type, post_url, date_created) ")
                    .literal(" VALUES(")
                    .commaSeparatedStrings(Arrays.asList(new String[]{
                            post.post_id, user_id, post.category, post.type, post.post_url, sqlDate.toString()}))
                    .literal(") ");
            QueryExecutor.execute(qb.build());

            FileWriter fw = new FileWriter();
            fw.writeFile(user_id, post.post_id, "text", post.content);
        }
        catch (Exception e) {
            System.out.print(e.getMessage());
            json.put("posted", "false");
            return okJSON(Response.Status.OK, json.toString(Constants.JSON_INDENT_FACTOR));
        }
        json.put("posted", "true");
        return okJSON(Response.Status.OK, json.toString(Constants.JSON_INDENT_FACTOR));
    }

    @GET
    @Path("user/{uid}/type/{type}/id/{pid}")
    public Response getUserFile(@PathParam("uid") String user_id, @PathParam("type") String type, @PathParam("pid") String post_id) {
        JSONObject json = new JSONObject();

        try {
            FileReader fw = new FileReader();
            String text = fw.readText(user_id, type, post_id);
            json.put("content", text);
        }
        catch (Exception e) {
            System.out.print(e.getMessage());
            return okJSON(Response.Status.OK, json.toString(Constants.JSON_INDENT_FACTOR));
        }
        return okJSON(Response.Status.OK, json.toString(Constants.JSON_INDENT_FACTOR));
    }
}
