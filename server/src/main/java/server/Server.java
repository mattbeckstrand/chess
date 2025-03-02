package server;

import dataaccess.GameData.MemoryGameDataDao;
import spark.*;
import handlers.*;
import dataaccess.Auth.*;
import dataaccess.User.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);
        MemoryUserDAO userDAO = new MemoryUserDAO();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();
        MemoryGameDataDao gameDAO = new MemoryGameDataDao();

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        //This line initializes the server and can be removed once you have a functioning endpoint
        Spark.post("/user", new RegisterHandler(authDAO, userDAO));
        Spark.post("/session", new LoginHandler(authDAO, userDAO));
        Spark.delete("/session", new LogoutHandler(authDAO));
        Spark.post("/game", new CreateGameHandler(gameDAO, authDAO));
        Spark.put("/game", new JoinGameHandler(gameDAO, authDAO));
        Spark.get("game", new ListGameHandler(authDAO, gameDAO));
        Spark.delete("/db", new DeleteHandler(gameDAO, authDAO, userDAO));


        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
