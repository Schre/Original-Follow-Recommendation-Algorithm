package server.restapi.notificationrestservice;

import org.json.JSONObject;
import server.database.queryengine.QueryExecutor;
import server.dto.dto.UserDTO;
import server.etc.Constants;
import server.notifications.ActionNotification;
import server.restapi.RestService;
import server.service.NotificationService;
import server.service.UserService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;


@Produces(MediaType.APPLICATION_JSON)
@Path("notifications")
public class NotificationRestService extends RestService {

    @GET
    @Path("{uid}")
    public Response getNotificationsForUser(@PathParam("uid") String user_id) {
        NotificationService service = new NotificationService();
        JSONObject json = new JSONObject();
        int i = 0;
        List<ActionNotification> notifications = service.getNotificationsForUser(user_id);
        for (ActionNotification notification : notifications) {
            JSONObject serial = notification.serialize();
            json.put(Integer.toString(i++), serial);
        }
        return okJSON(Response.Status.OK, json.toString(Constants.JSON_INDENT_FACTOR));
    }
}
