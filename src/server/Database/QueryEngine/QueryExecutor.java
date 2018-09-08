package server.Database.QueryEngine;

import server.etc.Constants;

import java.sql.*;

/**
 * TODO:
 * This is only for local db. We are only assuming the db is local for now.
 * We will want to expand this so that it can run queries against multiple
 * databases, but for now just running queries on our financial database will
 * suffice.
 *
 * We do not actually want this to have static functions. In the future, we will
 * instantiate this and it will get a connection and run the database query based
 * on the particular instantiated QueryExecutor's configuration.
 */

/**
 * TODO:
 * Use bonecp for database connection pool manager so that we do not have to keep
 * reconnecting to database for query (REEEEEEEEEEEEALLY SLOW!!!)
 */

public class QueryExecutor {

    private static Connection getLocalDbConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(Constants.getFinancialsDbName(true), Constants.adminUser, Constants.adminPassword);
            System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return conn;
    }

    public static ResultSet runQuery(String query) throws SQLException {
        Connection con = getLocalDbConnection();
        if (con == null) {
            throw new SQLException("Failed to establish a connection with the local database");
        }

        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        return rs;
    }

}
