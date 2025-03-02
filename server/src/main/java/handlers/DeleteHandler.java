package handlers;

import dataaccess.Auth.MemoryAuthDAO;
import dataaccess.GameData.MemoryGameDataDao;
import dataaccess.User.MemoryUserDAO;
import exception.ResponseException;
import spark.Route;
import spark.Response;
import spark.Request;

public class DeleteHandler implements Route {
    private final MemoryAuthDAO authDAO;
    private final MemoryUserDAO userDAO;
    private final MemoryGameDataDao gameDAO;

    public DeleteHandler(MemoryGameDataDao gameDAO, MemoryAuthDAO authDAO, MemoryUserDAO userDAO){
        this.authDAO = authDAO;
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;
    }

    public Object handle(Request req, Response res) throws ResponseException {

        try {
            authDAO.clear();
            userDAO.clear();
            gameDAO.clear();

            res.status(200);
            res.type("application/json");
            return "{}";
        } catch (Exception e) {
            throw new ResponseException(500, "Error: " + e.getMessage());
        }
    }
}
