package server.etc;

/**
 * Many of these should actually be configuration options!
 */
public class Constants {
    static public int JETTY_PORT_NUMBER = 7080;
    static public String API_PATH = "/api";
    static public String JDBC = "jdbc";
    static public String LOCAL_HOST = "localhost";
    static public String FINANCIALS_DB = "financial_db";
    static public String POSTGRE_SQL = "postgresql";
    static public String DB_HOST = "";
    static public int FINANCIALS_DB_PORT = 5432;

    /**
     * TODO:
     * We need to encrypt this at some point. This is find for now,
     * but obviously plain text is a bad idea. Also we need to come up
     * with a more complex user and pwd obviously.
     */

    static public String adminUser = "postgres";
    static public String adminPassword = "postgres";

    static public String getFinancialsDbName( boolean localHost ) {
        if (localHost) {
            return Constants.JDBC + ":" + POSTGRE_SQL + "://" + LOCAL_HOST + ':' + Integer.toString(FINANCIALS_DB_PORT)  + "/" + FINANCIALS_DB;
        }
        return Constants.JDBC + ":" + POSTGRE_SQL + "://" + DB_HOST + "/" + FINANCIALS_DB;
    }
}
