package server.restapi.databaserestservice;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import server.TrieHard.AutoComplete;
import server.TrieHard.TrieNode;
import server.database.queryengine.QueryBuilder;
import server.database.queryengine.QueryExecutor;
import server.dto.dto.UserDTO;
import server.etc.Constants;
import server.network.FollowerRecommendationSystem;
import server.network.NetworkNode;
import server.network.UserNetworkStatistics;
import server.restapi.RestService;
import server.service.UserService;
import server.shared.SharedObjects;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;


// TODO: All queries should be in UserService
@Produces(MediaType.APPLICATION_JSON)
@Path("users")
public class UserRestService extends RestService {
    @GET
    public Response getUsers() {
        JSONObject json = new JSONObject();

        try {
            json = QueryExecutor.runQuery("SELECT * FROM Users;");
        }
        catch (Exception e) {
            System.out.print(e.getMessage());
            return okJSON(Response.Status.OK, json.toString(Constants.JSON_INDENT_FACTOR));
        }
        return okJSON(Response.Status.OK, json.toString(Constants.JSON_INDENT_FACTOR));
    }

    @GET
    @Path("{uid}")
    public Response getUser(@PathParam("uid") String user_id) {
        JSONObject json = new JSONObject();

        try {
            json = QueryExecutor.runQuery("SELECT * FROM Users WHERE user_id=" + "\"" + user_id + "\"");
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            //return okJSON(Response.Status.INTERNAL_SERVER_ERROR, json.toString(Constants.JSON_INDENT_FACTOR));
        }
        return okJSON(Response.Status.OK, json.toString(Constants.JSON_INDENT_FACTOR));
    }

    @Path("{user_id}/followings")
    @GET
    public Response getUserFollowingSet(@PathParam("user_id") String user_id) {
        Set<UserDTO> followingSet = (new UserService()).getUserFollowingSet(user_id);
        JSONObject ret = new JSONObject();
        JSONObject response = new JSONObject();


        ObjectMapper serializer = new ObjectMapper();
        int i = 0;
        for (UserDTO user : followingSet) {
            try {
                String temp = serializer.writeValueAsString(user);
                response.put(Integer.toString(i), new JSONObject(temp));

            }
            catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            ++i;
        }

        ret.put("response", response);
        return okJSON(Response.Status.OK, ret.toString(Constants.JSON_INDENT_FACTOR));
    }
    /* internal */
    public Response getUserFollowings(String user_id) {
        JSONObject json = new JSONObject();
        
        try {
            /* Select all of the follwers for our user */
            String query = "SELECT Users.* FROM " +
                    "(SELECT following_id FROM Users,Followings WHERE Users.user_id=" + "\"" + user_id + "\" " +
                    "AND Users.user_id=Followings.user_id) temp" +
                    "," +
                    "Users " +
                    "WHERE Users.user_id=temp.following_id";
            json = QueryExecutor.runQuery(query);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            //return okJSON(Response.Status.INTERNAL_SERVER_ERROR, json.toString(Constants.JSON_INDENT_FACTOR));
        }
        return okJSON(Response.Status.OK, json.toString(Constants.JSON_INDENT_FACTOR));
    }

    //TODO: test this
    @DELETE
    @Path("{uid}/followings/{fid}")
    public Response deleteFollowing(@PathParam("uid") String uid, @PathParam("fid") String fid) {
        // Post new following
        QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder = queryBuilder.delete().from().literal("Followings ").where()
                .literal(" user_id=").string(uid).and().literal(" following_id=").string(fid);

        JSONObject obj = new JSONObject();

        try {
            QueryExecutor.execute(queryBuilder.build());
        }
        catch (SQLException e) {
            e.printStackTrace();
            obj.put("posted", "false");
            return okJSON(Response.Status.OK, obj.toString(Constants.JSON_INDENT_FACTOR));
        }

        obj.put("posted", "true");
        return okJSON(Response.Status.OK, obj.toString(Constants.JSON_INDENT_FACTOR));
    }

    @POST
    @Path("{uid}/followings/{fid}")
    public Response followUser(@PathParam("uid") String uid, @PathParam("fid") String fid) {
        // Post new following
        QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder = queryBuilder.insert().into().literal("Followings ").literal("(user_id, following_id) ")
                .literal("VALUES ( ").commaSeparatedStrings(new ArrayList<>(Arrays.asList(new String[]{
                        uid, fid
        }))).literal(" )");

        JSONObject obj = new JSONObject();

        try {
            QueryExecutor.execute(queryBuilder.build());
        }
        catch (SQLException e) {
            e.printStackTrace();
            obj.put("posted", "false");
            return okJSON(Response.Status.OK, obj.toString(Constants.JSON_INDENT_FACTOR));
        }

        obj.put("posted", "true");
        return okJSON(Response.Status.OK, obj.toString(Constants.JSON_INDENT_FACTOR));
    }

    /*
        Assume content is correct format
     */
    @POST
    public Response createUser(String content) {
        JSONObject userJson = new JSONObject(content);
        ObjectMapper serializer = new ObjectMapper();
        Exception ex = null;
        String user_id = "";
        try {
            UserDTO userDTO = serializer.readValue(userJson.toString(), UserDTO.class);
            userDTO.username = userDTO.email;
            userDTO.user_id = UserDTO.generateUserId();
            String dateCreated = new SimpleDateFormat("yyyy-MM-dd").format((new Date()));
            userDTO.date_created = dateCreated;

            if (userDTO.birth_date == null) {
                userDTO.birth_date = dateCreated;
            }

            List<String> fields = new ArrayList<>(Arrays.asList(new String[]{userDTO.user_id, userDTO.email, userDTO.username, userDTO.password, userDTO.first_name,
            userDTO.last_name, userDTO.gender.toString(), userDTO.profile_picture_url, userDTO.birth_date, userDTO.date_created, userDTO.field}));

            QueryBuilder builder = new QueryBuilder();

            builder = builder.insert().into().literal("Users ").literal("(user_id, email, username, password, first_name, last_name, gender, profile_picture_url, birth_date, date_created, field)")
                    .literal(" Values ( ").commaSeparatedStrings(fields).literal(")");
            QueryExecutor.execute(builder.build());
            user_id = userDTO.user_id;

        }catch (JsonGenerationException e) {
            ex = e;
            e.printStackTrace();
        }
        catch (JsonMappingException e){
            ex = e;
            e.printStackTrace();
        }
        catch (IOException e) {
            ex = e;
            e.printStackTrace();
        }
        catch (Exception e) {
            ex = e;
            e.printStackTrace();
        }

        JSONObject res = new JSONObject();

        // send error response
        if (ex != null) {
            res.put("posted", "false");
            return okJSON(Response.Status.OK, res.toString(Constants.JSON_INDENT_FACTOR));
        }
        res.put("posted", "true");
        res.put("user_id", user_id);
        return okJSON(Response.Status.OK, res.toString(Constants.JSON_INDENT_FACTOR));
    }

    @GET
    @Path("{uid}/searchforuser")
    public Response searchForBlank(@PathParam("uid") String uid) {
        return getTopKFollowRecommendations(uid, 10);
    }
    @GET
    @Path("{uid}/searchforuser/{query}")
    public Response searchForUser(@PathParam("uid") String uid, @PathParam("query") String query) {
        AutoComplete ac = SharedObjects.getUserQueryAutoComplete();
        JSONObject json = new JSONObject();

        if (!ac.containsUser(uid)) {
            populateUserTrie(uid);
        }

        Set<TrieNode<String, String>> res = ac.getNodesMatchingPattern(uid, query);

        int hit = 0;
        for (TrieNode node : res) {
            JSONObject val = new JSONObject();
            val.put("name", node.getKey());
            val.put("user_id", node.getValue());
            json.put(Integer.toString(hit), val);
            ++hit;
        }
        JSONObject response = new JSONObject();
        response.put("response", json);
        return okJSON(Response.Status.OK, response.toString(Constants.JSON_INDENT_FACTOR));
    }

    @GET
    @Path("{uid}/followrecommendations/{K}")
    public Response getTopKFollowRecommendations(@PathParam("uid") String uid, @PathParam("K") int K) {
        JSONObject ret = new JSONObject();
        /*
            TODO: Extract a sample of our user network from db and use FollowerRecommendationSystem to return best matches!
            For now we will extract a network consisting of our user, user's follower's, and user's follower's follower's
            In the future, we should only sample a maximum of a certain amount of data to reduce computation time and number of database server.queries
         */
        UserDTO user = (new UserService()).getUser(uid);
        NetworkNode root = new NetworkNode(user.user_id, user.field);
        FollowerRecommendationSystem frs = new FollowerRecommendationSystem(root);
        frs.loadNetworkForUser();
        List<NetworkNode> topK = frs.getTopKRecommendations(K);

        UserService userService = new UserService();
        int indx = K;
        for (NetworkNode node : topK) {
            --K;
            UserDTO userDTO = userService.getUser(node.getUID());
            JSONObject obj = new JSONObject();
            obj.put("user_id", node.getUID());
            obj.put("field", node.getField());
            obj.put("mutual_followings", node.getMutualFollowings());
            obj.put("first_name", userDTO.first_name);
            obj.put("last_name", userDTO.last_name);
            obj.put("gender", userDTO.gender);
            obj.put("username", userDTO.username);
            ret.put(Integer.toString(K), obj);
        }

        //Map<String, Double> stats = UserNetworkStatistics.computeFieldPercentages(new HashSet<>(topK));

        /*JSONObject statistics = new JSONObject();
        for (String profession : stats.keySet()) {
            Double percentage = stats.get(profession);
            statistics.put(profession, percentage);
        }

        ret.put("statistics", statistics);*/

        JSONObject response = new JSONObject();
        response.put("response", ret);
        return okJSON(Response.Status.OK, response.toString(Constants.JSON_INDENT_FACTOR));
    }

    private void populateUserTrie(String uid) {
        // populate trie with recommendations, and followings, and top professional's first and last name
        AutoComplete ac = SharedObjects.getUserQueryAutoComplete();
        UserService service = new UserService();
        UserDTO user = service.getUser(uid);
        NetworkNode root = new NetworkNode(user.user_id, user.field);
        FollowerRecommendationSystem frs = new FollowerRecommendationSystem(root);
        frs.loadNetworkForUser();
        List<NetworkNode> topK = frs.getTopKRecommendations(1000);

        Set<UserDTO> followings = service.getUserFollowings(uid);

        /* Add topK to trie */
        for (NetworkNode node : topK) {
            UserDTO temp = service.getUser(node.getUID());
            ac.add(uid, temp.user_id, temp.first_name + " " + temp.last_name);
        }

        /* Add followings to trie */
        for (UserDTO following : followings) {
            ac.add(uid, following.user_id, following.first_name + " " + following.last_name);
        }
    }
}
