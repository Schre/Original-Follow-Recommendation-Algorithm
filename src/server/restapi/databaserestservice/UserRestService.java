package server.restapi.databaserestservice;

import org.json.JSONObject;
import server.database.queryengine.QueryExecutor;
import server.dto.Serializers.TransactionItemSerializer;
import server.dto.dto.TransactionalItemDTO;
import server.dto.dto.UserDTO;
import server.etc.Constants;
import server.network.FollowerRecommendationSystem;
import server.network.NetworkNode;
import server.restapi.RestService;
import server.service.UserService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

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
            return okJSON(Response.Status.INTERNAL_SERVER_ERROR, json.toString(Constants.JSON_INDENT_FACTOR));
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

    // TODO: Fix this query

    @GET
    @Path("{uid}/followings")
    public Response getUserFollowings(@PathParam("uid") String user_id) {
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

        int indx = K;
        for (NetworkNode node : topK) {
            JSONObject obj = new JSONObject();
            obj.put("user_id", node.getUID());
            obj.put("field", node.getField());
            obj.put("mutual_followings", node.getMutualFollowings());
            ret.put(Integer.toString(K), obj);
            --K;
        }
        return okJSON(Response.Status.OK, ret.toString(Constants.JSON_INDENT_FACTOR));
    }
}
