import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import server.etc.Constants;
import server.restapi.databaserestservice.*;
import shared.SharedObjects;

/**
 Register your rest services here! ( in registerRestServices() )
 */

public class RestServer {
    public static void main(String[] args) throws Exception {
        // Allowed Origins:
        FilterHolder filterHolder = new FilterHolder(CrossOriginFilter.class);

        // TODO: Tweak this to only allow UI as an origin
        filterHolder.setInitParameter("allowedOrigins", "*");
        filterHolder.setInitParameter("allowedMethods", "GET, POST, PUT");


        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath(Constants.API_PATH);
        context.addFilter(filterHolder, "/*", null);

        Server jettyServer = new Server(Constants.JETTY_PORT_NUMBER);
        jettyServer.setHandler(context);

        ServletHolder jerseyServlet = context.addServlet(
                org.glassfish.jersey.servlet.ServletContainer.class, "/*");
        jerseyServlet.setInitOrder(0);

        registerRestServices(jerseyServlet);

        // Initialized shared objects
        SharedObjects.initialize();

        try {
            jettyServer.start();
            jettyServer.join();
        } finally {
            jettyServer.destroy();
        }
    }

    /* Add rest service to the map to register it */
    private static void registerRestServices(ServletHolder jerseyServlet) {
/*        restServices.put("jersey.config.server.provider.classnames.HelloRestService",
                HelloRestService.class.getCanonicalName());
        restServices.put("jersey.config.server.provider.classname.QueryRestServices",
                QueryRestService.class.getCanonicalName());
*/

        jerseyServlet.setInitParameter(
                "jersey.config.server.provider.classnames",
                        UserRestService.class.getCanonicalName());
    }
}
