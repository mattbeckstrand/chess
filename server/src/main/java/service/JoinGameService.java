package service;
import dataaccess.DataAccessException;
import dataaccess.auth.AuthDAO;
import dataaccess.auth.MemoryAuthDAO;
import dataaccess.gamedata.GameDataDAO;
import dataaccess.gamedata.MemoryGameDataDao;
import exception.ResponseException;
import model.*;

import java.util.Objects;

public class JoinGameService {
    private final GameDataDAO gameDataDao;
    private final AuthDAO authDao;
    private final String authToken;
    private final Integer gameId;
    private final String playerColor;

    public JoinGameService(GameDataDAO gameDataDao, String authToken, AuthDAO authDao, int gameId, String playerColor){
        this.gameDataDao = gameDataDao;
        this.authDao = authDao;
        this.authToken = authToken;
        this.gameId = gameId;
        this.playerColor = playerColor;
    }

    public String getUserName() throws ResponseException, DataAccessException {
        try {
            AuthData auth = authDao.findAuthByToken(authToken);
            if (auth == null) {
                throw new ResponseException(401, "Error: unauthorized");
            }
            return auth.username();
        } catch (DataAccessException e) {
            throw new ResponseException(500, "Database error: " + e.getMessage());
        }
    }

    public GameData joinGame() throws ResponseException, DataAccessException {
        if (playerColor == null || (!playerColor.equals("WHITE") && !playerColor.equals("BLACK"))) {
            throw new ResponseException(400, "Error: Invalid player color");
        }

        String username = this.getUserName();
        System.out.println(username);

        try {
            GameData game = gameDataDao.getGame(gameId);
            if (game == null) {
                throw new ResponseException(400, "Error: game not found");
            }

            if (playerColor.equals("WHITE") && (game.getWhiteUsername() != null && !game.getWhiteUsername().equals(username))) {
                throw new ResponseException(403, "Error: white player slot is already taken");
            }
            if (playerColor.equals("BLACK") && game.getBlackUsername() != null) {
                throw new ResponseException(403, "Error: black player slot is already taken");
            }

            if (playerColor.equals("WHITE")) {
                gameDataDao.addPlayer(gameId, username, "WHITE");
            } else {
                gameDataDao.addPlayer(gameId, username, "BLACK");
            }

            return gameDataDao.getGame(gameId);
        } catch (DataAccessException e) {
            throw new ResponseException(500, "Database error: " + e.getMessage());
        }
    }
}

