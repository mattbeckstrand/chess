package handlers;

import dataaccess.DataAccessException;
import dataaccess.auth.AuthDAO;
import dataaccess.auth.MemoryAuthDAO;
import dataaccess.gamedata.GameDataDAO;
import dataaccess.gamedata.MemoryGameDataDao;
import dataaccess.user.MemoryUserDAO;
import dataaccess.user.UserDAO;
import exception.ResponseException;
import spark.Route;
import spark.Response;
import spark.Request;

public class DeleteHandler implements Route {
    private final AuthDAO authDAO;
    private final UserDAO userDAO;
    private final GameDataDAO gameDAO;

    public DeleteHandler(GameDataDAO gameDAO, AuthDAO authDAO, UserDAO userDAO){
        this.authDAO = authDAO;
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;
    }

    public Object handle(Request req, Response res){

        try {
            authDAO.clear();
            userDAO.clear();
            gameDAO.clear();

            res.status(200);
            res.type("application/json");
            return "{}";
        } catch (DataAccessException e) {
            res.status(500);
            return "{Error in DB: " + e.getMessage() + " }";
        } catch (Exception e) {
            res.status(500);
            return "{Error in internal server: " + e.getMessage() + " }";
        }
    }
}
