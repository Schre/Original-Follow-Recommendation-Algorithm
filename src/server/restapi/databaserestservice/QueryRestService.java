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
public class QueryRestService extends RestService {
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


    /*public Response postTransactionalItem(String body) {
        TransactionalItemDTO co = null;
        JSONObject json = new JSONObject();

        try {
            TransactionItemSerializer serializer = new TransactionItemSerializer();
            co = serializer.deserialize(body);
            QueryExecutor.insertTransactionalItem(co);
            json.put("posted", "true");
        } catch (Exception e) {
            System.out.print(e.getMessage());
            json.put("posted", "false");
            return okJSON(Response.Status.INTERNAL_SERVER_ERROR, json.toString(Constants.JSON_INDENT_FACTOR));
        }
        return okJSON(Response.Status.OK, json.toString(Constants.JSON_INDENT_FACTOR));
    }*/
}
