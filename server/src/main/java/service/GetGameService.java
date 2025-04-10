package service;

import dataaccess.DataAccessException;
import dataaccess.auth.AuthDAO;
import dataaccess.gamedata.GameDataDAO;
import exception.ResponseException;
import model.*;
import service.ListGamesService;

public class GetGameService {
    private final String authToken;
    private final AuthDAO authDAO;
    private final GameDataDAO gameDAO;
    private final Integer gameId;

    public GetGameService(String authToken, AuthDAO authDAO, GameDataDAO gameDAO, int gameId){
        this.authToken = authToken;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
        this.gameId = gameId;

    }

    public GameData getGameData() throws ResponseException {
        ListGamesService service = new ListGamesService(authToken, authDAO, gameDAO);
        service.checkAuth();
        try {
            return gameDAO.getGame(gameId);
        } catch (DataAccessException e) {
            throw new ResponseException(500, "Database error: " + e.getMessage());
        }
    }
}