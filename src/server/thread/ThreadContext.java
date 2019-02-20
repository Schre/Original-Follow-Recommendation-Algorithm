package server.thread;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;
import server.etc.Constants;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * All objects which a server.thread will need access to
 * will be instantiated in server.thread context.
 *
 */
public class ThreadContext {
    private BoneCP financialDbConnectionPool;

    private static ThreadLocal threadLocal = new ThreadLocal() {
        @Override
        protected ThreadContext initialValue() {
            return new ThreadContext();
        }
    };

    public ThreadContext() {
        setUpFinancialDbConnectionPool();
    }

    public static ThreadContext get() {
        return (ThreadContext) threadLocal.get();
    }

    public void setFinancialDbConnectionPool(BoneCP connectionPool) {
        try {
            this.financialDbConnectionPool = new BoneCP(connectionPool.getConfig());
        } catch (SQLException e) {
            System.err.println("Failed to set FinancialDbConnectionPool");
        }
    }

    public BoneCP getFinancialDbConnectionPool() {
        return financialDbConnectionPool;
    }

    private void setUpFinancialDbConnectionPool() {
        BoneCP connectionPool = null;
        Connection connection = null;
        try {
            // Set up the connection pool
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
                this.financialDbConnectionPool = connectionPool;
            } else {
                System.err.println("Failed to establish a connection with database: " + Constants.getDbPath(true));
                return;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
