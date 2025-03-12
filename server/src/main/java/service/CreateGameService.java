package service;
import dataaccess.DataAccessException;
import dataaccess.auth.AuthDAO;
import dataaccess.auth.MemoryAuthDAO;
import dataaccess.gamedata.GameDataDAO;
import dataaccess.gamedata.MemoryGameDataDao;
import exception.ResponseException;
import model.*;


public class CreateGameService {
    private final GameDataDAO gameDataDao;
    private final AuthDAO authDao;
    private final String authToken;
    private final String gameName;

    public CreateGameService(GameDataDAO gameDataDao, String authToken, AuthDAO authDao, String gameName){
        this.gameDataDao = gameDataDao;
        this.authDao = authDao;
        this.authToken = authToken;
        this.gameName = gameName;
    }

    public void checkAuth() throws ResponseException {
        try {
            AuthData auth = authDao.findAuthByToken(authToken);
            if (auth == null) {
                throw new ResponseException(401, "Error: unauthorized");
            }
        } catch (DataAccessException e) {
            throw new ResponseException(500, "Database error: " + e.getMessage());
        }
    }

    public GameData addGameData() throws ResponseException {
        this.checkAuth();
        try {
            return gameDataDao.createGame(gameName);
        } catch (DataAccessException e) {
            throw new ResponseException(500, "Database error: " + e.getMessage());
        }
    }




}
