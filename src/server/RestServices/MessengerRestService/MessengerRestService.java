package server.RestServices.MessengerRestService;

import org.json.JSONObject;
import server.RestServices.RestService;
import server.Database.QueryEngine.QueryExecutor;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.ResultSet;

@Produces(MediaType.APPLICATION_JSON)
@Path("messenger")
public class MessengerRestService extends RestService {
    @GET
    @Path("hello")
    public Response printHelloWorld() {
        String message = "";
        JSONObject json = new JSONObject();
        try {
           ResultSet rs = QueryExecutor.runQuery("SELECT message FROM hello_world where code='00000'");
           // rs is not pointing to any row... position it to first row
            rs.next();
            message = rs.getString(1);
            json.put( "Message", message );
        }
        catch ( Exception e ) {
            System.out.print( e.getMessage() );
        }
        return okJSON(Response.Status.OK, json.toString());
    }
}
