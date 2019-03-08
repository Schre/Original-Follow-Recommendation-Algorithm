package server.etc;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * We should consider splitting these constants up into different files.
 * I.e., database related constants in one file, jetty related in another.
 */
public class Constants {
    static public int JETTY_PORT_NUMBER = 7080;
    static public String API_PATH = "/api";
    static public String JDBC = "jdbc";
    static public String LOCAL_HOST = "localhost";

    /***/
    static public String DB_HOST = "";
    static public int CONNCETION_POOL_MAX_CONNECTIONS_PER_PARTITION = 10;
    static public int CONNECTION_POOL_MIN_CONNECTIONS_PER_PARTITION = 5;
    static public int CONNECTION_POOL_PARTITION_COUNT = 1;
    static public String PASSWORD_HASH_BLOWFISH = "2a";

    static public int JSON_INDENT_FACTOR = 4;

    static public String adminUser = "admin";
    static public String adminPassword = "admin";

    static public String getDbPath(boolean localHost) {
       // if (localHost) {
            return Constants.JDBC + ":" + "mysql" + "://" + LOCAL_HOST + ':' + "3306"  + "/" + "socialdb";
        //}
    }
}
