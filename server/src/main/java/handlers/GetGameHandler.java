package handlers;

import com.google.gson.Gson;
import dataaccess.auth.AuthDAO;
import dataaccess.gamedata.GameDataDAO;
import exception.ResponseException;
import model.ErrorResponse;
import model.GameData;
import service.GetGameService;
import spark.Route;
import spark.Request;
import spark.Response;

public class GetGameHandler implements Route {
    ;
    private final AuthDAO authDAO;
    private final GameDataDAO gameDAO;

    public GetGameHandler(AuthDAO authDAO, GameDataDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    @Override
    public Object handle(Request req, Response res) {
        Gson gson = new Gson();
        try {
            String authToken = req.headers("authorization");
            int gameId = Integer.parseInt(req.params("gameId"));
            GetGameService service = new GetGameService(authToken, authDAO, gameDAO, gameId);
            GameData game = service.getGameData();
            res.type("application/json");
            res.status(200);
            return gson.toJson(game);
        } catch (ResponseException e) {
            res.status(e.statusCode());
            return gson.toJson(new ErrorResponse("error: " + e.getMessage()));
        } catch (Exception e) {
            res.status(500);
            return gson.toJson(new ErrorResponse("internal server error"));
        }
    }
}
