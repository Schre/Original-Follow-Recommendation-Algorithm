package server.shared;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;
import server.etc.Constants;
import server.TrieHard.*;
import server.network.FollowerRecommendationSystem;
import server.network.RelatednessMatrix;
import server.notifications.ActionNotificationManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * All objects which all threads will need to share
 * will be instantiated in SharedObjects.
 */
public class SharedObjects {
    private static BoneCP connectionPool;
    private static Set<String> loggedInUsers;
    private static AutoComplete userQueryAutoComplete;
    private static ActionNotificationManager actionNotificationManager;

    public static synchronized void initialize() {
        setUpconnectionPool();
        userQueryAutoComplete = new AutoComplete();
        RelatednessMatrix.initialize();
        actionNotificationManager = new ActionNotificationManager();
    }

    public static AutoComplete getUserQueryAutoComplete() {
        return userQueryAutoComplete;
    }

    public static synchronized void setconnectionPool(BoneCP connectionPool) {
        try {
            SharedObjects.connectionPool = new BoneCP(connectionPool.getConfig());
        } catch (SQLException e) {
            System.err.println("Failed to set connectionPool");
        }
    }

    public static boolean loginUser(String username) {
        if (loggedInUsers != null) {
            if (!loggedInUsers.contains(username)) {
                loggedInUsers.add(username);
                return true;
            }
            return false;
        }
        loggedInUsers = new HashSet<>();
        loggedInUsers.add(username);
        return true;
    }

    public static ActionNotificationManager getActionNotificationManager() {
        return actionNotificationManager;
    }

    public static boolean logoutUser(String username) {
        if (loggedInUsers != null && loggedInUsers.contains(username)) {
            loggedInUsers.remove(username);
            return true;
        }
        return false;
    }

    public static boolean userIsLoggedIn(String username) {
        if (loggedInUsers != null) {
            return loggedInUsers.contains(username);
        }
        return false;
    }
    public static BoneCP getConnectionPool() {
        return connectionPool;
    }

    private static synchronized void setUpconnectionPool() {
        BoneCP connectionPool = null;
        Connection connection = null;
        try {
            // Set up the connection pool
            try {
                Class.forName("com.mysql.jdbc.Driver");
            }
            catch (Exception e){
                e.printStackTrace();
            }
            BoneCPConfig config = new BoneCPConfig();
            config.setJdbcUrl(Constants.getDbPath(true));
            config.setUsername(Constants.adminUser);
            config.setPassword(Constants.adminPassword);
            config.setMinConnectionsPerPartition(Constants.CONNECTION_POOL_MIN_CONNECTIONS_PER_PARTITION);
            config.setMaxConnectionsPerPartition(Constants.CONNCETION_POOL_MAX_CONNECTIONS_PER_PARTITION);
            config.setPartitionCount(Constants.CONNECTION_POOL_PARTITION_COUNT);
            connectionPool = new BoneCP(config);

            connection = connectionPool.getConnection(); // fetch a connection

            if (connection != null) {
                // Assign connection pool to current server.thread
                System.out.println("Successfully connected to database: " + Constants.getDbPath(true));
                SharedObjects.connectionPool = connectionPool;
            } else {
                System.err.println("Failed to establish a connection with database: " + Constants.getDbPath(true));
                return;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
