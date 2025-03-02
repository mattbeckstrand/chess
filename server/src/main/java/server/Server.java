package server;

import spark.*;
import handlers.*;
import dataaccess.Auth.*;
import dataaccess.User.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);
        MemoryUserDao userDAO = new MemoryUserDao();
        MemoryAuthDAO authDAO = new MemoryAuthDAO();

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        //This line initializes the server and can be removed once you have a functioning endpoint
        Spark.post("/user", new RegisterHandler(authDAO, userDAO));

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
