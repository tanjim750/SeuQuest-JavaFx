package bd.edu.seu.chat.seuquest.user;

import java.sql.*;

public class DatabaseManager {
    private static String DATABASE_URL;
    private static String DATABASE_USER;
    private static String DATABASE_PASSWORD;
    private Connection connection;
    private Statement statement;

    public DatabaseManager(String DATABASE_URL, String DATABASE_USER, String DATABASE_PASSWORD) throws SQLException {
        this.DATABASE_URL = DATABASE_URL;
        this.DATABASE_USER = DATABASE_USER;
        this.DATABASE_PASSWORD = DATABASE_PASSWORD;

        this.connection = getConnection();
        this.statement = connection.createStatement();
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
    }

    public boolean createTable(String tableName,String tableColumn) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE "
                + tableName + "("
                +"id BIGINT PRIMARY KEY AUTO_INCREMENT,"
                +tableColumn + ")");
        System.out.println(query.toString());
        return statement.execute(query.toString());
    }

    public boolean dropTable(String tableName) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("DROP TABLE " + tableName);
        System.out.println(query.toString());
        return statement.execute(query.toString());
    }

    public ResultSet getAll(String tableName) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM " + tableName);
        System.out.println(query.toString());
        return statement.executeQuery(query.toString());
    }

    public ResultSet getById(String id, String tableName) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM " + tableName + " WHERE id = " + id);
        System.out.println(query.toString());
        return statement.executeQuery(query.toString());
    }

    public ResultSet getByCustomCondition(String tableName, String condition) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM " + tableName + " WHERE " + condition);
        System.out.println(query.toString());
        return statement.executeQuery(query.toString());
    }

    public ResultSet insert(String tableName, String value) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO " + tableName + " VALUES (" + value + ")");
        System.out.println(query.toString());
        return statement.executeQuery(query.toString());
    }

    public ResultSet updateById(String tableName, String id, String values) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("UPDATE " + tableName + " SET " + values + " WHERE id = " + id);
        System.out.println(query.toString());
        return statement.executeQuery(query.toString());
    }

    public boolean deleteById(String id, String tableName) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("DELETE FROM " + tableName + " WHERE id = " + id);
        System.out.println(query.toString());
        return statement.execute(query.toString());
    }

    public boolean customCommand(String command) throws SQLException {
        System.out.println(command);
        return statement.execute(command);
    }

    public ResultSet customQuery(String query) throws SQLException {
        System.out.println(query);
        return statement.executeQuery(query);
    }
}
