package handlers;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.auth.AuthDAO;
import dataaccess.auth.MemoryAuthDAO;
import dataaccess.gamedata.GameDataDAO;
import dataaccess.gamedata.MemoryGameDataDao;
import exception.ResponseException;
import model.ErrorResponse;
import model.GameSummary;
import model.ListGamesResponse;
import service.ListGamesService;
import spark.Route;
import spark.Request;
import spark.Response;

import javax.xml.crypto.Data;
import java.util.List;

public class ListGameHandler implements Route {
    ;
    private final AuthDAO authDAO;
    private final GameDataDAO gameDAO;

    public ListGameHandler(AuthDAO authDAO, GameDataDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    @Override
    public Object handle(Request req, Response res) {
        Gson gson = new Gson();
        try {
            String authToken = req.headers("authorization");
            ListGamesService service = new ListGamesService(authToken, authDAO, gameDAO);
            List<GameSummary> games = service.listGameData();
            ListGamesResponse response = new ListGamesResponse(games);
            res.type("application/json");
            res.status(200);
            return gson.toJson(response);
        } catch (ResponseException e) {
            res.status(e.statusCode());
            return gson.toJson(new ErrorResponse("error: " + e.getMessage()));
        } catch (Exception e) {
            res.status(500);
            return gson.toJson(new ErrorResponse("internal server error"));
        }
    }
}
