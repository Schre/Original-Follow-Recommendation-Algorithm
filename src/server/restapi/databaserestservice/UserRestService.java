package server.restapi.databaserestservice;

import org.json.JSONObject;
import server.database.queryengine.QueryExecutor;
import server.dto.Serializers.TransactionItemSerializer;
import server.dto.dto.TransactionalItemDTO;
import server.dto.dto.UserDTO;
import server.etc.Constants;
import server.restapi.RestService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Produces(MediaType.APPLICATION_JSON)
@Path("query")
public class UserRestService extends RestService {
    @GET
    @Path("users")
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
    @Path("users/{uid}/followrecommendations/{K}")
    public Response getTopKFollowRecommendations(@PathParam("uid") String uid, @PathParam("K") int K) {
        /*
            TODO: Extract a sample of our user network from db and use FollowerRecommendationSystem to return best matches!
            For now we will extract a network consisting of our user, user's follower's, and user's follower's follower's
            In the future, we should only sample a maximum of a certain amount of data to reduce computation time and number of database queries
         */
        return null;
    }
}
