package server.database.queryengine;

import org.json.JSONObject;
import server.dto.Serializers.TransactionItemSerializer;
import server.dto.Serializers.UserSerializer;
import server.dto.dto.TransactionalItemDTO;
import server.dto.dto.UserDTO;
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

    static public UserDTO getUserGivenUsername(String username) {
        UserDTO ret = null;
        JSONObject queryReturnValue = null;
        String query = new QueryBuilder()
                .select()
                .star()
                .from()
                .addString(Constants.ACCOUNT)
                .where()
                .addString("username")
                .equals()
                .addString(username)
                .build();

        try {
            queryReturnValue = QueryExecutor.runQuery(query).getJSONObject("obj0");
            ret = new UserSerializer().deserialize(queryReturnValue.toString());
            /* TODO: Set cost object attributes based on return value */

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }

    // given transaction name and uid
    static public TransactionalItemDTO getTransactionalItemForUserByTransactionID(String uid, String transaction) {
        TransactionalItemDTO ret = null;
        /*String query = "SELECT * FROM account, transactionalItem where account." + Constants.USR_ID + " = transactionalItem." + Constants.USR_ID + " AND account.user_id = "
                + uid + " and transactionalItem_id = " + cid + ";";*/
        JSONObject queryReturnValue = null;
        List<String> tablesToJoin = new ArrayList<>();
        tablesToJoin.add(Constants.ACCOUNT);
        tablesToJoin.add(Constants.TRANSACTIONAL_ITEM_TABLE);
        String query = new QueryBuilder()
                .select()
                .star()
                .from()
                .cartesianProduct(tablesToJoin)
                .where()
                .addString(Constants.ACCOUNT)
                .accessMember(Constants.ID)
                .equals()
                .addString(Constants.TRANSACTIONAL_ITEM_TABLE)
                .accessMember(Constants.USR_ID)
                .and()
                .addString(Constants.TRANSACTIONAL_ITEM_TABLE)
                .accessMember(Constants.USR_ID)
                .equals()
                .addString(uid)
                .and()
                .addString(Constants.TRANSACTIONAL_ITEM_TABLE)
                .accessMember(Constants.ID)
                .equals()
                .addString(transaction)
                .build();

        try {
            queryReturnValue = QueryExecutor.runQuery(query).getJSONObject("obj0");
            ret = new TransactionItemSerializer().deserialize(queryReturnValue.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // run query

        return ret;
    }

    // given uid and transaction id
    static public List<TransactionalItemDTO> getTransactionalItemsForUser(String uid) {
        List<TransactionalItemDTO> ret = new ArrayList<>();
        /*String query = "SELECT * FROM account, transactionalItem where account." + Constants.USR_ID + " = transactionalItem." + Constants.USR_ID + " AND account.user_id = "
                + uid + " and transactionalItem_id = " + cid + ";";*/
        JSONObject queryReturnValue = null;
        List<String> tablesToJoin = new ArrayList<>();
        tablesToJoin.add(Constants.ACCOUNT);
        tablesToJoin.add(Constants.TRANSACTIONAL_ITEM_TABLE);
        String query = new QueryBuilder()
                .select()
                .star()
                .from()
                .cartesianProduct(tablesToJoin)
                .where()
                .addString(Constants.ACCOUNT)
                .accessMember(Constants.ID)
                .equals()
                .addString(Constants.TRANSACTIONAL_ITEM_TABLE)
                .accessMember(Constants.USR_ID)
                .and()
                .addString(Constants.TRANSACTIONAL_ITEM_TABLE)
                .accessMember(Constants.USR_ID)
                .equals()
                .addString(uid)
                .build();

        try {
            queryReturnValue = QueryExecutor.runQuery(query);
            for (String key : queryReturnValue.keySet()) {
                ret.add(new TransactionItemSerializer().deserialize(queryReturnValue.getJSONObject(key).toString()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // run query

        return ret;
    }

    static public boolean insertTransactionalItem(TransactionalItemDTO co) {
        String query = "insert into transactionalItem (transaction, date, amount, description" +
                ", paymentFrequency, category, id, uid)" +
                " VALUES (" + co.transaction + "," + co.date + "," + co.amount
                + "," + co.description + ", " + co.paymentFrequency + ", " +
                co.category + "," + co.id + "," + co.uid + ")";

        try {
            runQuery(query);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

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
            int objCount = 0;
            while (rs.next()) {
                int numColumns = rsmd.getColumnCount();
                JSONObject tmp = new JSONObject();
                for (index = 0; index < numColumns; ++index) {
                    String columnName = rsmd.getColumnName(index + 1);
                    tmp.put(columnName, rs.getArray(index + 1).toString());
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
}
