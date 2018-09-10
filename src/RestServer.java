import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import server.etc.Constants;
import server.restapi.hellorestservice.HelloRestService;
import shared.SharedObjects;

/**
 Register your rest services here! ( in registerRestServices() )
 */

public class RestServer {
    public static void main(String[] args) throws Exception {
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath(Constants.API_PATH);

        Server jettyServer = new Server(Constants.JETTY_PORT_NUMBER);
        jettyServer.setHandler(context);

        ServletHolder jerseyServlet = context.addServlet(
                org.glassfish.jersey.servlet.ServletContainer.class, "/*");
        jerseyServlet.setInitOrder(0);

        registerRestServices(jerseyServlet);

        // Initialized shared objects
        SharedObjects.initialize();
        ;
        try {
            jettyServer.start();
            jettyServer.join();
        } finally {
            jettyServer.destroy();
        }
    }

    private static void registerRestServices(ServletHolder jerseyServlet) {
        jerseyServlet.setInitParameter(
                "jersey.config.server.provider.classnames",
                HelloRestService.class.getCanonicalName());
    }
}