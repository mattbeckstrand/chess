package service;

import dataaccess.DataAccessException;
import dataaccess.auth.AuthDAO;
import dataaccess.auth.MemoryAuthDAO;
import dataaccess.gamedata.GameDataDAO;
import dataaccess.gamedata.MemoryGameDataDao;
import exception.ResponseException;
import model.*;
import java.util.stream.Collectors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListGamesService {
    private final String authToken;
    private final AuthDAO authDAO;
    private final GameDataDAO gameDAO;

    public ListGamesService(String authToken, AuthDAO authDAO, GameDataDAO gameDAO){
        this.authToken = authToken;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public void checkAuth() throws ResponseException {
        try {
            AuthData auth = authDAO.findAuthByToken(authToken);
            if (auth == null) {
                throw new ResponseException(401, "Error: unauthorized");
            }
        } catch (DataAccessException e) {
            throw new ResponseException(500, "Database error: " + e.getMessage());
        }
    }

    public List<GameSummary> listGameData() throws ResponseException {
        this.checkAuth();
        try {
            HashMap<Integer, GameData> games = gameDAO.listGames();
            return games.values().stream()
                    .map(game -> new GameSummary(game.getGameID(), game.getGameName(), game.getWhiteUsername(), game.getBlackUsername()))
                    .collect(Collectors.toList());
        } catch (DataAccessException e) {
            throw new ResponseException(500, "Database error: " + e.getMessage());
        }
    }
}
