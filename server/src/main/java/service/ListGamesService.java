package service;

import dataaccess.auth.MemoryAuthDAO;
import dataaccess.gamedata.MemoryGameDataDao;
import exception.ResponseException;
import model.*;
import java.util.stream.Collectors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListGamesService {
    private final String authToken;
    private final MemoryAuthDAO authDAO;
    private final MemoryGameDataDao gameDAO;

    public ListGamesService(String authToken, MemoryAuthDAO authDAO, MemoryGameDataDao gameDAO){
        this.authToken = authToken;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public void checkAuth() throws ResponseException {
        AuthData auth = authDAO.findAuthByToken(authToken);
        if (auth == null) {
            throw new ResponseException(401, "Error: unauthorized");
        }
    }

    public List<GameSummary> listGameData() throws ResponseException{
        this.checkAuth();
        HashMap<Integer, GameData> games = gameDAO.listGames();

        ArrayList<GameData> gameDataList = new ArrayList<>(games.values());



        return gameDataList.stream()
                .map(game -> new GameSummary(game.getGameID(), game.getGameName(), game.getWhiteUsername(), game.getBlackUsername()))
                .collect(Collectors.toList());
    }

}
