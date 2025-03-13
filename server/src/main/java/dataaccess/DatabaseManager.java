package dataaccess;

import java.sql.*;
import java.util.Properties;


public class DatabaseManager {
    private static final String DATABASE_NAME;
    private static final String USER;
    private static final String PASSWORD;
    private static final String CONNECTION_URL;

    /*
     * Load the database information for the db.properties file.
     */
    static {
        try {
            try (var propStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties")) {
                if (propStream == null) {
                    throw new Exception("Unable to load db.properties");
                }
                Properties props = new Properties();
                props.load(propStream);
                DATABASE_NAME = props.getProperty("db.name");
                USER = props.getProperty("db.user");
                PASSWORD = props.getProperty("db.password");

                var host = props.getProperty("db.host");
                var port = Integer.parseInt(props.getProperty("db.port"));
                CONNECTION_URL = String.format("jdbc:mysql://%s:%d", host, port);
            }
        } catch (Exception ex) {
            throw new RuntimeException("unable to process db.properties. " + ex.getMessage());
        }
    }

    /**
     * Creates the database if it does not already exist.
     */
    public static void createDatabase() throws DataAccessException {
        try {
            var statement = "CREATE DATABASE IF NOT EXISTS " + DATABASE_NAME;
            var conn = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD);
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
            conn.setCatalog(DATABASE_NAME);
            createTables(conn);
            System.out.println("DB created, tables also created.");
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    /**
     * Create a connection to the database and sets the catalog based upon the
     * properties specified in db.properties. Connections to the database should
     * be short-lived, and you must close the connection when you are done with it.
     * The easiest way to do that is with a try-with-resource block.
     * <br/>
     * <code>
     * try (var conn = DbInfo.getConnection(databaseName)) {
     * // execute SQL statements.
     * }
     * </code>
     */
    public static Connection getConnection() throws DataAccessException {
        try {
            var conn = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD);
            conn.setCatalog(DATABASE_NAME);
            return conn;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public static void createTables(Connection conn) throws DataAccessException {
        String createAuthTable = "CREATE TABLE IF NOT EXISTS auth (authToken VARCHAR(225) PRIMARY KEY, +" +
                "username VARCHAR(225) NOT NULL)";
        String createUserTable = "CREATE TABLE IF NOT EXISTS users" +
                "(username VARCHAR(100) PRIMARY KEY, password VARCHAR(225) NOT NULL, email VARCHAR(225))";
        String createGameTable = "CREATE TABLE IF NOT EXISTS games (gameID INTEGER PRIMARY KEY, +" +
                " whiteUsername VARCHAR(225), blackUsername VARCHAR(225), gameName VARCHAR(225) NOT NULL, game TEXT NOT NULL)";

        try (var authStmt = conn.prepareStatement(createAuthTable);
             var userStmt = conn.prepareStatement(createUserTable);
             var gameStmt = conn.prepareStatement(createGameTable)) {
            authStmt.executeUpdate();
            userStmt.executeUpdate();
            gameStmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("error creating tables: " + e.getMessage());
        }
    }
}
