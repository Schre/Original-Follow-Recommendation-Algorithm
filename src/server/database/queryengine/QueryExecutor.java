package server.database.queryengine;

import org.json.JSONObject;
import server.dto.Serializers.CostObjectSerializer;
import server.dto.dto.CostObjectDTO;
import server.etc.Constants;
import shared.SharedObjects;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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


    static public CostObjectDTO getCostObjectForUser(String uid, String cid) {
        CostObjectDTO ret = null;
        /*String query = "SELECT * FROM account, cost_obj where account." + Constants.USR_ID + " = cost_obj." + Constants.USR_ID + " AND account.user_id = "
                + uid + " and cost_obj_id = " + cid + ";";*/
        JSONObject queryReturnValue = null;
        List<String> tablesToJoin = new ArrayList<>();
        tablesToJoin.add(Constants.ACCOUNT);
        tablesToJoin.add(Constants.COST_OBJECT);
        String query = new QueryBuilder()
                .select()
                .star()
                .from()
                .cartesianProduct(tablesToJoin)
                .where()
                .addString(Constants.ACCOUNT)
                .accessMember(Constants.USR_ID)
                .equals()
                .addString(Constants.COST_OBJECT)
                .accessMember(Constants.USR_ID)
                .and()
                .addString(Constants.COST_OBJECT)
                .accessMember(Constants.USR_ID)
                .equals()
                .addString(uid)
                .and()
                .addString(Constants.COST_OBJECT)
                .accessMember(Constants.COST_OBJ_ID)
                .equals()
                .addString(cid)
                .build();

        try {
            queryReturnValue = QueryExecutor.runQuery(query);
            ret = new CostObjectSerializer().deserialize(queryReturnValue.toString());
            /* TODO: Set cost object attributes based on return value */

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // run query

        return ret;
    }

    static public boolean insertCostObject(CostObjectDTO co) {
        String query = "insert into cost_obj (user_id, username, password)" +
                " VALUES (" + co.name + "," + co.user_id + "," + co.cost
                + "," + co.user_id + ")";

        try {
            runQuery(query);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /* TODO: Return json. Find a way to convert result set to JSON */
    public static JSONObject runQuery(String query) throws SQLException {
        Statement stmt = null;
        Connection con = null;
        ResultSet rs = null;
        JSONObject jsonObject = new JSONObject();
        int index = 0;
        ArrayList<String> ret = new ArrayList<>();
        try {
            con = getFinancialDbConnection();
            if (con == null) {
                throw new SQLException("Failed to establish a connection with the local database");
            }

            stmt = con.createStatement();
            rs = stmt.executeQuery(query);

            // Use this for gettting col names
            ResultSetMetaData rsmd = rs.getMetaData();

            while (rs.next()) {
                int numColumns = rsmd.getColumnCount();
                for (index = 0; index < numColumns; ++index) {
                    String columnName = rsmd.getColumnName(index + 1);
                    jsonObject.put(columnName, rs.getArray(index + 1).toString());
                }
                //ret.add(rs.getArray(++index).toString()); // columns start from 1
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

}
