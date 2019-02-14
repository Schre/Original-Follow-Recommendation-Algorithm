package server.etc;

/**
 * We should consider splitting these constants up into different files.
 * I.e., database related constants in one file, jetty related in another.
 */
public class Constants {
    static public int JETTY_PORT_NUMBER = 7080;
    static public String API_PATH = "/api";
    static public String JDBC = "jdbc";
    static public String LOCAL_HOST = "localhost";

    /*** Query Constants
     * These should really be moved to their own file at some point.
     * */
    static public String TRANSACTION = "transaction";
    static public String ID = "id";
    static public String USR_ID = "uid";
    static public String TRANSACTIONAL_ITEM_TABLE = "transactionalItem";
    static public String ACCOUNT = "account";
    /***/
    static public String DB_HOST = "";
    static public int CONNCETION_POOL_MAX_CONNECTIONS_PER_PARTITION = 10;
    static public int CONNECTION_POOL_MIN_CONNECTIONS_PER_PARTITION = 5;
    static public int CONNECTION_POOL_PARTITION_COUNT = 1;
    static public String PASSWORD_HASH_BLOWFISH = "2a";

    /**
     * TODO:
     * We need to encrypt this at some point. This is fine for now,
     * but obviously plain text is a bad idea. Also we need to come up
     * with a more complex user and pwd.
     *
     * In the future, each individual user will have their own unique username and pwd,
     * but for now we will do all of our development with our admin account.
     */

    static public int JSON_INDENT_FACTOR = 4;

    static public String adminUser = "admin";
    static public String adminPassword = "admin";

    static public String getDbPath(boolean localHost) {
       // if (localHost) {
            return Constants.JDBC + ":" + "mysql" + "://" + LOCAL_HOST + ':' + "3306"  + "/" + "socialdb";
        //}
    }
}
