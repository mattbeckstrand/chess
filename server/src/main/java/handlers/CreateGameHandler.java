package handlers;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.auth.AuthDAO;
import dataaccess.auth.MemoryAuthDAO;
import dataaccess.gamedata.GameDataDAO;
import dataaccess.gamedata.MemoryGameDataDao;
import exception.ResponseException;
import model.*;
import service.CreateGameService;
import spark.Route;
import spark.Response;
import spark.Request;



public class CreateGameHandler implements Route {
    private final GameDataDAO gameDataDao;
    private final AuthDAO authDao;

    public CreateGameHandler(GameDataDAO gameDataDao, AuthDAO authDao){
        this.gameDataDao = gameDataDao;
        this.authDao = authDao;
    }

    public Object handle(Request req, Response res) {
        Gson gson = new Gson();
        try {
            String authToken = req.headers("Authorization");
            CreateGameRequest request = gson.fromJson(req.body(), CreateGameRequest.class);
            CreateGameService service = new CreateGameService(gameDataDao, authToken, authDao, request.gameName());
            GameData game = service.addGameData();
            String jsonResponse = gson.toJson(new CreateGameResponse(game.getGameID()));

            res.status(200);
            res.type("application/json");
            return jsonResponse;
        } catch (ResponseException e) {
            res.status(e.statusCode());
            return gson.toJson(new ErrorResponse("error: " + e.getMessage()));
        } catch (Exception e) {
            res.status(500);
            return gson.toJson(new ErrorResponse("internal server error"));
        }
    }

}
