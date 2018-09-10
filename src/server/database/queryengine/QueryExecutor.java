package server.database.queryengine;

import shared.SharedObjects;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

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


public class QueryExecutor {

    private static Connection getFinancialDbConnection() {
        Connection conn = null;
        try {
            conn = SharedObjects.getFinancialDbConnectionPool().getConnection();
            System.out.println("Obtained connection from connection pool.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return conn;
    }

    public static ArrayList<String> runQuery(String query) {
        Statement stmt = null;
        Connection con = null;
        ResultSet rs = null;
        int index = 0;
        ArrayList<String> ret = new ArrayList<>();
        try {
            con = getFinancialDbConnection();
            if (con == null) {
                throw new SQLException("Failed to establish a connection with the local database");
            }

            stmt = con.createStatement();
            rs = stmt.executeQuery(query);

            while (rs.next()) {
                ret.add(rs.getArray(++index).toString()); // columns start from 1
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            /* Close an clean up connection. Upon closing this connection, we will
               return the connection back to the connection pool.
             */
            try {
                stmt.close();
            } catch (SQLException e) {

            }
            try {
                rs.close();
            } catch (SQLException e) {

            }
            try {
                con.close();
            } catch (SQLException e) {

            }
        }
        return ret;
    }

}
