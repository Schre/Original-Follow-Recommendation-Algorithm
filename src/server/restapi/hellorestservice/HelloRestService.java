package server.restapi.hellorestservice;

import org.json.JSONObject;
import server.database.queryengine.QueryExecutor;
import server.restapi.RestService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Produces(MediaType.APPLICATION_JSON)
@Path("messenger")
public class HelloRestService extends RestService {
    @GET
    @Path("hello")
    public Response printHelloWorld() {
        JSONObject json = new JSONObject();
        try {
            json = QueryExecutor.runQuery("SELECT message FROM hello_world where code='00000'");
        }
        catch ( Exception e ) {
            System.out.print( e.getMessage() );
        }
        return okJSON(Response.Status.OK, json.toString());
    }
}
