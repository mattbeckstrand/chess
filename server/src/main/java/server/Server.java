package server;

import dataaccess.DataAccessException;
import dataaccess.gamedata.MemoryGameDataDao;
import dataaccess.gamedata.SqlGameDataDAO;
import exception.ResponseException;
import spark.*;
import handlers.*;
import dataaccess.auth.*;
import dataaccess.user.*;
import dataaccess.DatabaseManager;

import javax.xml.crypto.Data;
import java.sql.Connection;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);
        SqlUserDAO userDAO = new SqlUserDAO();
        SqlAuthDao authDAO = null;
        SqlGameDataDAO gameDAO = new SqlGameDataDAO();

        Spark.staticFiles.location("web");
        try {
            authDAO = new SqlAuthDao();
            DatabaseManager.createDatabase();
        } catch (DataAccessException e){
            System.out.println("Error in auth or creating Database");
        }
        // Register your endpoints and handle exceptions here.

        //This line initializes the server and can be removed once you have a functioning endpoint
        Spark.post("/user", new RegisterHandler(authDAO, userDAO));
        Spark.post("/session", new LoginHandler(authDAO, userDAO));
        Spark.delete("/session", new LogoutHandler(authDAO));
        Spark.post("/game", new CreateGameHandler(gameDAO, authDAO));
        Spark.put("/game", new JoinGameHandler(gameDAO, authDAO));
        Spark.get("/game", new ListGameHandler(authDAO, gameDAO));
        Spark.delete("/db", new DeleteHandler(gameDAO, authDAO, userDAO));


        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
