package service;
import dataaccess.auth.MemoryAuthDAO;
import dataaccess.gamedata.MemoryGameDataDao;
import exception.ResponseException;
import model.*;


public class CreateGameService {
    private final MemoryGameDataDao gameDataDao;
    private final MemoryAuthDAO authDao;
    private final String authToken;
    private final String gameName;

    public CreateGameService(MemoryGameDataDao gameDataDao, String authToken, MemoryAuthDAO authDao, String gameName){
        this.gameDataDao = gameDataDao;
        this.authDao = authDao;
        this.authToken = authToken;
        this.gameName = gameName;
    }

    public void checkAuth() throws ResponseException {
        AuthData auth = authDao.findAuthByToken(authToken);
        if (auth == null) {
            throw new ResponseException(401, "Error: unauthorized");
        }
    }

    public GameData addGameData() throws ResponseException {
        this.checkAuth();
        return gameDataDao.createGame(gameName);
    }




}
