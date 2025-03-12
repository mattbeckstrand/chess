package handlers;

import com.google.gson.Gson;
import dataaccess.auth.AuthDAO;
import dataaccess.auth.MemoryAuthDAO;
import dataaccess.gamedata.GameDataDAO;
import dataaccess.gamedata.MemoryGameDataDao;
import exception.ResponseException;
import model.*;
import service.JoinGameService;
import spark.Route;
import spark.Response;
import spark.Request;



public class JoinGameHandler implements Route {
    private final GameDataDAO gameDataDao;
    private final AuthDAO authDao;

    public JoinGameHandler(GameDataDAO gameDataDao, AuthDAO authDao){
        this.gameDataDao = gameDataDao;
        this.authDao = authDao;
    }

    public Object handle(Request req, Response res) throws ResponseException {
        Gson gson = new Gson();
        try {
            String authToken = req.headers("Authorization");
            JoinGameRequest request = gson.fromJson(req.body(), JoinGameRequest.class);
            JoinGameService service = new JoinGameService(gameDataDao, authToken, authDao, request.gameID(), request.playerColor());
            GameData game = service.joinGame();
            res.type("application/json");
            res.status(200);
            return "{}";
        } catch (ResponseException e) {
            res.status(e.statusCode());
            return gson.toJson(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            res.status(500);
            return gson.toJson(new ErrorResponse("Internal server error: " + e.getMessage()));
        }
    }

}
