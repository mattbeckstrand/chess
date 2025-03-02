package service;
import dataaccess.Auth.MemoryAuthDAO;
import dataaccess.GameData.MemoryGameDataDao;
import dataaccess.User.MemoryUserDAO;
import exception.ResponseException;
import model.*;

public class JoinGameService {
    private final MemoryGameDataDao gameDataDao;
    private final MemoryAuthDAO authDao;
    private final String authToken;
    private final Integer gameId;
    private final String playerColor;

    public JoinGameService(MemoryGameDataDao gameDataDao, String authToken, MemoryAuthDAO authDao, int gameId, String playerColor){
        this.gameDataDao = gameDataDao;
        this.authDao = authDao;
        this.authToken = authToken;
        this.gameId = gameId;
        this.playerColor = playerColor;
    }

    public String getUserName() throws ResponseException {
        AuthData auth = authDao.findAuthByToken(authToken);
        if (auth == null) {
            throw new ResponseException(401, "Error: unauthorized");
        }
        return auth.username();
    }

    public GameData joinGame() throws ResponseException{
        if (playerColor == null || (!playerColor.equals("WHITE") && !playerColor.equals("BLACK"))) {
            System.out.println("error in player color");
            throw new ResponseException(400, "Error: bad request");
        }

        String username = this.getUserName();
        if (username == null) {
            throw new ResponseException(401, "Error: unauthorized");
        }

        GameData game = gameDataDao.getGame(gameId);
        System.out.println(game);
        if (game == null) {
            throw new ResponseException(400, "Error: bad request");
        }

        if (playerColor.equals("WHITE")) {
            if (game.getWhiteUsername() != null) {
                throw new ResponseException(403, "Error: already taken");
            }
            gameDataDao.addWhitePlayer(gameId, username);
        } else {
            if (game.getBlackUsername() != null) {
                throw new ResponseException(403, "Error: already taken");
            }
            gameDataDao.addBlackPlayer(gameId, username);
        }

        return game;
    }
}

