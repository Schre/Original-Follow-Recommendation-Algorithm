package server.restapi.financialqueryrestservice;

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
@Path("financialdb")
public class QueryRestService extends RestService {
    @POST
    @Path("transaction")
    public Response postTransactionalItem(String body) {
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
    }


    @GET
    @Path("transaction/user/{uid}/objects/{cid}")
    public Response getTransactionalItemByTransactionalItemId(@PathParam("uid") String uid, @PathParam("cid") String cid) {
        TransactionalItemDTO co = null;
        JSONObject json = null;

        try {
            co = QueryExecutor.getTransactionalItemForUserByTransactionID(uid, cid);
            TransactionItemSerializer cos = new TransactionItemSerializer();
            json = new JSONObject(cos.serialize(co));
        } catch (Exception e) {
            System.out.print(e.getMessage());
            return okJSON(Response.Status.INTERNAL_SERVER_ERROR, json.toString(Constants.JSON_INDENT_FACTOR));
        }
        return okJSON(Response.Status.OK, json.toString(Constants.JSON_INDENT_FACTOR));
    }

    @GET
    @Path("transaction/user/{uid}")
    public Response getTransactionalItems(@PathParam("uid") String uid) {
        List<TransactionalItemDTO> co = null;
        JSONObject json = new JSONObject();

        try {
            co = QueryExecutor.getTransactionalItemsForUser(uid);
            TransactionItemSerializer cos = new TransactionItemSerializer();
            JSONObject tmp = null;
            int objNumber = 0;
            for (TransactionalItemDTO item : co) {
                tmp = new JSONObject(cos.serialize(co.get(objNumber)));
                json.put("obj" + Integer.toString(objNumber++), tmp);
            }
        } catch (Exception e) {
            System.out.print(e.getMessage());
            return okJSON(Response.Status.INTERNAL_SERVER_ERROR, json.toString(Constants.JSON_INDENT_FACTOR));
        }
        return okJSON(Response.Status.OK, json.toString(Constants.JSON_INDENT_FACTOR));
    }


    // TODO: Implement this
    @POST
    @Path("user")
    public Response postUser(String body) {
        UserDTO userDTO = null;
        JSONObject json = null;

        return okJSON(Response.Status.OK, json.toString(Constants.JSON_INDENT_FACTOR));
    }
}
