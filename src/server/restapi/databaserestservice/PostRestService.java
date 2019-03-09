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
import server.service.PostService;
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
        List<PostDTO> posts = (new PostService()).getPosts(user_id, true);
        JSONObject json = new JSONObject();
        int id = 0;
        for (PostDTO post : posts) {
            try {
                JSONObject postJson = new JSONObject((new ObjectMapper()).writeValueAsString(post));
                json.put(Integer.toString(++id), postJson);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        return okJSON(Response.Status.OK, json.toString(Constants.JSON_INDENT_FACTOR));
    }



    @GET
    @Path("user/{uid}/recent/{K}")
    public Response getKMostRecentPostsByUser(@PathParam("uid") String user_id, @PathParam("K") int K) {
        return null;
    }
    @GET
    @Path("user/{uid}/id/{pid}")
    public Response getPostByUser(@PathParam("uid") String user_id, @PathParam("pid") String post_id) {
        PostDTO post = (new PostService()).getPost(user_id, post_id, true);
        ObjectMapper mapper = new ObjectMapper();

        try {
            JSONObject json = new JSONObject(mapper.writeValueAsString(post));
            return okJSON(Response.Status.OK, json.toString(Constants.JSON_INDENT_FACTOR));
        }
        catch (Exception e) {
            e.printStackTrace();
            return okJSON(Response.Status.INTERNAL_SERVER_ERROR, "{}");
        }
    }

    @GET
    @Path("user/{uid}/id/{pid}/contentless")
    public Response getContentlessPostByUser(@PathParam("uid") String user_id, @PathParam("pid") String post_id) {
        PostDTO post = (new PostService()).getPost(user_id, post_id, false);
        ObjectMapper mapper = new ObjectMapper();

        try {
            JSONObject json = new JSONObject(mapper.writeValueAsString(post));
            return okJSON(Response.Status.OK, json.toString(Constants.JSON_INDENT_FACTOR));
        }
        catch (Exception e) {
            e.printStackTrace();
            return okJSON(Response.Status.INTERNAL_SERVER_ERROR, "{}");
        }
    }


    @GET
    @Path("user/{uid}/feed")
    public Response getUserFeed(@PathParam("uid") String user_id) {
        UserService us = new UserService();
        JSONObject ret = new JSONObject();

        Set<UserDTO> followings = us.getUserFollowings(user_id);
        List<PostDTO> posts = new ArrayList<>();

        //TODO: PostService should handle this
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
        String timestamp = Long.toString(new java.util.Date().getTime());
        String pid = PostDTO.generateId();
        boolean success;
        try {
            PostDTO post = (new ObjectMapper()).readValue(content, PostDTO.class);
            post.post_id = pid;
            post.date_created = timestamp;
            post.user_id = user_id;
            success = (new PostService()).createPost(post);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.print(e.getMessage());
            success = false;
        }
        json.put("posted", success);
        return getContentlessPostByUser(user_id, pid);
    }

    @GET
    @Path("user/{uid}/type/{type}/id/{pid}")
    public Response getUserFile(@PathParam("uid") String user_id, @PathParam("type") String type, @PathParam("pid") String post_id) {
        JSONObject json = new JSONObject();

        try {
            FileReader fw = new FileReader();
            String text = fw.readFile(user_id, type, post_id);
            json.put("content", text);
        }
        catch (Exception e) {
            System.out.print(e.getMessage());
            return okJSON(Response.Status.OK, json.toString(Constants.JSON_INDENT_FACTOR));
        }
        return okJSON(Response.Status.OK, json.toString(Constants.JSON_INDENT_FACTOR));
    }

    @GET
    @Path("user/{uid}/type/{type}")
    public Response getAllUserFiles(@PathParam("uid") String user_id, @PathParam("type") String type) {
        JSONObject json = new JSONObject();

        try {
            FileReader fw = new FileReader();
            //String text = fw.readText(user_id, type, post_id);
            //json.put("content", text);
        }
        catch (Exception e) {
            System.out.print(e.getMessage());
            return okJSON(Response.Status.OK, json.toString(Constants.JSON_INDENT_FACTOR));
        }
        return okJSON(Response.Status.OK, json.toString(Constants.JSON_INDENT_FACTOR));
    }
}
