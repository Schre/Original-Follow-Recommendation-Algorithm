package server.restapi.notificationrestservice;

import org.json.JSONObject;
import server.database.queryengine.QueryExecutor;
import server.etc.Constants;
import server.restapi.RestService;
import server.service.NotificationService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Produces(MediaType.APPLICATION_JSON)
@Path("notifications")
public class NotificationRestService extends RestService {

    @GET
    @Path("{user_id}")
    public Response getNotificationsForUser(@PathParam("user_id") String user_id) {
        NotificationService service = new NotificationService();
        //return okJSON(Response.Status.OK, json.toString(Constants.JSON_INDENT_FACTOR));
        return null;
    }
}
