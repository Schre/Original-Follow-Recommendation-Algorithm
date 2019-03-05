package server.restapi.etc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import server.database.queryengine.QueryBuilder;
import server.database.queryengine.QueryExecutor;
import server.dto.dto.PostDTO;
import server.dto.dto.UserDTO;
import server.etc.Constants;
import server.network.RelatednessMatrix;
import server.restapi.RestService;
import server.service.UserService;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Path("matrix")
public class RelatednessMatrixRestService extends RestService {

    @GET
    public Response getRelatednessMatrix() {
        JSONObject json = new JSONObject();
        int relation = 0;
        for (String f1 : RelatednessMatrix.getSupportedFields()) {
            for (String f2: RelatednessMatrix.getSupportedFields()) {

                // TODO: This is a hack. These fields should be supported... fix this!
                if (f1.equals("undefined") || f2.equals("undefined")) {
                    continue;
                }

                JSONObject relatedness = new JSONObject();
                relatedness.put("from", f1);
                relatedness.put("to", f2);
                relatedness.put("relatedness", RelatednessMatrix.getRelatedness(f1, f2));
                json.put("relation" + Integer.toString(relation), relatedness);
                ++relation;
            }
        }
        return okJSON(Response.Status.OK, json.toString(Constants.JSON_INDENT_FACTOR));
    }
}
