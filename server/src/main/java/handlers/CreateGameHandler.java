package handlers;

import com.google.gson.Gson;
import dataaccess.Auth.MemoryAuthDAO;
import dataaccess.GameData.MemoryGameDataDao;
import dataaccess.User.MemoryUserDAO;
import exception.ResponseException;
import model.*;
import service.CreateGameService;
import spark.Route;
import spark.Response;
import spark.Request;



public class CreateGameHandler implements Route {
    private final MemoryGameDataDao gameDataDao;
    private final MemoryAuthDAO authDao;

    public CreateGameHandler(MemoryGameDataDao gameDataDao, MemoryAuthDAO authDao){
        this.gameDataDao = gameDataDao;
        this.authDao = authDao;
    }

    public Object handle(Request req, Response res) throws ResponseException {
        Gson gson = new Gson();
        String authToken = req.headers("Authorization");
        CreateGameRequest request = gson.fromJson(req.body(), CreateGameRequest.class);
        CreateGameService service = new CreateGameService(gameDataDao, authToken, authDao, request.gameName());
        GameData game = service.addGameData();
        res.status(200);
        res.type("application/json");
        return gson.toJson(new CreateGameResponse(game.getGameID()));
    }

}
