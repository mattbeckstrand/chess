package handlers;

import com.google.gson.Gson;
import dataaccess.Auth.MemoryAuthDAO;
import dataaccess.GameData.MemoryGameDataDao;
import exception.ResponseException;
import model.ErrorResponse;
import model.GameData;
import model.GameSummary;
import model.ListGamesResponse;
import service.ListGamesService;
import spark.Route;
import spark.Request;
import spark.Response;

import java.util.List;

public class ListGameHandler implements Route{ ;
    private final MemoryAuthDAO authDAO;
    private final MemoryGameDataDao gameDAO;

    public ListGameHandler(MemoryAuthDAO authDAO, MemoryGameDataDao gameDAO){
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    @Override
    public Object handle(Request req, Response res) throws ResponseException {
        Gson gson = new Gson();
        String authToken = req.headers("authorization");
        try {
            ListGamesService service = new ListGamesService(authToken, authDAO, gameDAO);
            List<GameSummary> games = service.listGameData();
            ListGamesResponse response = new ListGamesResponse(games);
            res.type("application/json");
            res.status(200);
            return gson.toJson(response);
        } catch (ResponseException e) {
            res.status(400);
            return gson.toJson(new ErrorResponse("error: " + e.getMessage()));
        } catch (Exception e) {
            res.status(500);
            return gson.toJson(new ErrorResponse("internal server error"));
        }
    }
}
