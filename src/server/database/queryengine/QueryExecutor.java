package server.database.queryengine;

import org.json.JSONObject;
import shared.SharedObjects;

import java.sql.*;
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

    private static Connection getConnection() {
        Connection conn = null;
        try {
            conn = SharedObjects.getConnectionPool().getConnection();
            System.out.println("Obtained connection from connection pool.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return conn;
    }


    public static JSONObject runQuery(String query) throws SQLException {
        Statement stmt = null;
        Connection con = null;
        ResultSet rs = null;
        JSONObject jsonObject = new JSONObject();
        int index = 0;
        ArrayList<String> ret = new ArrayList<>();
        try {
            con = getConnection();
            if (con == null) {
                throw new SQLException("Failed to establish a connection with the local database");
            }

            stmt = con.createStatement();
            rs = stmt.executeQuery(query);

            // Use this for gettting col names
            ResultSetMetaData rsmd = rs.getMetaData();
            int objCount = 0;
            while (rs.next()) {
                int numColumns = rsmd.getColumnCount();
                JSONObject tmp = new JSONObject();
                for (index = 0; index < numColumns; ++index) {
                    String columnName = rsmd.getColumnName(index + 1);
                    Object obj = rs.getObject(index + 1);

                    if (obj == null) {
                        continue;
                    }

                    tmp.put(columnName, obj.toString());
                }
                // add this object to the main json object
                jsonObject.put("obj" + Integer.toString(objCount++), tmp);
            }
        } catch (Exception e) {
            System.err.println("Error executing query: " + e.getMessage());
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
        return jsonObject;
    }

    public static boolean execute(String query) throws SQLException {
        Statement stmt = null;
        Connection con = null;
        boolean executed = false;

        try {
            con = getConnection();
            if (con == null) {
                throw new SQLException("Failed to establish a connection with the local database");
            }

            stmt = con.createStatement();
            executed =  stmt.execute(query);

        } catch (Exception e) {
            System.err.println("Error executing query: " + e.getMessage());
        } finally {
            /* Close an clean up connection. Upon closing this connection, we will
               return the connection back to the connection pool.
             */
            try {
                stmt.close();
            } catch (SQLException e) {

            }
            }
            try {
                con.close();
            } catch (SQLException e) {

            }

            return executed;
        }
}
