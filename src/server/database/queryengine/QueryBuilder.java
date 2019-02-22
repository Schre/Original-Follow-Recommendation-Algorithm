package server.database.queryengine;


import java.util.List;

// Use this to construct server.queries. The server.queries will execute through the QueryExecutor
public class QueryBuilder {
    private String query = "";

    public QueryBuilder(String query) {
        this.query = query;
    }

    public QueryBuilder() {
        this.query = "";
    }

    public String build() {
        return query + ';';
    }

    public QueryBuilder select() {
        return new QueryBuilder(query + "SELECT ");
    }

    public QueryBuilder delete() {
        return new QueryBuilder(query + "DELETE ");
    }

    public QueryBuilder insert() {
        return new QueryBuilder(query + "INSERT ");
    }

    public QueryBuilder into() {
        return new QueryBuilder(query + "INTO ");
    }


    public QueryBuilder where() {
        return new QueryBuilder(query + "WHERE ");
    }

    public QueryBuilder equals() {
        return new QueryBuilder(query + "=");
    }

    public QueryBuilder string(String s) {
        return new QueryBuilder(query + "\"" + s + "\"");
    }

    public QueryBuilder literal(String s) {
        return new QueryBuilder(query + s);
    }


    public QueryBuilder accessMember(String member) {
        return new QueryBuilder(query + "." + member + " ");
    }

    public QueryBuilder and() {
        return new QueryBuilder(query + " AND ");
    }

    public QueryBuilder or() {
        return new QueryBuilder(query + " OR ");
    }

    public QueryBuilder not() {
        return new QueryBuilder(query + " NULL ");
    }

    public QueryBuilder nullw() {
        return new QueryBuilder(query + " NULL ");
    }

    public QueryBuilder is() {
        return new QueryBuilder(query + " IS ");
    }

    public QueryBuilder in() {
        return new QueryBuilder(query + " in ");
    }

    public QueryBuilder commaSeparatedStrings(List<String> tablesToJoin) {
        for (int i = 0; i < tablesToJoin.size(); ++i) {
            String table = tablesToJoin.get(i);
            if (i < tablesToJoin.size() - 1) {
                query += "\"" + table + "\"" + ',';
            } else {
                query += "\"" + table + "\"" + " ";
            }
        }
        return new QueryBuilder(query);
    }

    public QueryBuilder star() {
        return new QueryBuilder(query + " * ");
    }

    public QueryBuilder from() {
        return new QueryBuilder(query + " FROM ");
    }

}
