package server.restapi.financialqueryrestservice;

import org.json.JSONObject;
import server.database.queryengine.QueryExecutor;
import server.dto.Serializers.CostObjectSerializer;
import server.dto.dto.CostObjectDTO;
import server.restapi.RestService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Produces(MediaType.APPLICATION_JSON)
@Path("financialdb")
public class QueryRestService extends RestService {
    @POST
    @Path("costobject")
    public Response postCostObject(String body) {
        CostObjectDTO co = null;
        JSONObject json = new JSONObject();

        try {
            CostObjectSerializer serializer = new CostObjectSerializer();
            co = serializer.deserialize(body);
            QueryExecutor.insertCostObject(co);
            json.put("posted", "true");
        } catch (Exception e) {
            System.out.print(e.getMessage());
            json.put("posted", "false");
            return okJSON(Response.Status.INTERNAL_SERVER_ERROR, json.toString());
        }
        return okJSON(Response.Status.OK, json.toString());
    }

    @GET
    @Path("costobject/user/{uid}/objects/{cid}")
    public Response getCostObjectByCostObjectId(@PathParam("uid") String uid, @PathParam("cid") String cid) {
        CostObjectDTO co = null;
        JSONObject json = null;

        try {
            co = QueryExecutor.getCostObjectForUser(uid, cid);
            CostObjectSerializer cos = new CostObjectSerializer();
            json = new JSONObject(cos.serialize(co));
        } catch (Exception e) {
            System.out.print(e.getMessage());
            return okJSON(Response.Status.INTERNAL_SERVER_ERROR, json.toString());
        }
        return okJSON(Response.Status.OK, json.toString());
    }
}
