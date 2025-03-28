package service;

import dataaccess.DataAccessException;
import dataaccess.auth.MemoryAuthDAO;
import dataaccess.gamedata.MemoryGameDataDao;
import dataaccess.user.MemoryUserDAO;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.LoginRequest;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class CreateGameServiceTest {
    private MemoryAuthDAO authDao;
    private MemoryGameDataDao gameDataDao;
    private MemoryUserDAO userDao;


    @BeforeEach
    void setUp() {
        authDao = new MemoryAuthDAO();
        gameDataDao = new MemoryGameDataDao();
        userDao = new MemoryUserDAO();
    }

    @Test
    @DisplayName("Game created success")
    public void testGameCreated() throws DataAccessException, ResponseException {
        userDao.addUser(new UserData("testKing", "kingoftests12", "test@example.com"));

        LoginRequest request = new LoginRequest("testKing", "kingoftests12");
        LoginService logService = new LoginService(authDao, userDao, request);

        AuthData auth = logService.addAuth();
        String authToken = auth.authToken();

        CreateGameService gameService = new CreateGameService(gameDataDao, authToken, authDao, "gameName");
        GameData game = gameService.addGameData();
        Assertions.assertEquals(1, game.getGameID(), "Id's dont match");
    }

    @Test
    @DisplayName("Unauthorized Game Creation")
    public void testUnauthorizedGameCreation() throws ResponseException, DataAccessException {
        userDao.addUser(new UserData("testKing", "kingoftests12", "test@example.com"));

        LoginRequest request = new LoginRequest("testKing", "kingoftests12");
        LoginService logService = new LoginService(authDao, userDao, request);

        AuthData auth = logService.addAuth();
        String authToken = auth.authToken();

        CreateGameService gameService = new CreateGameService(gameDataDao, authToken, authDao, "gameName");
        authDao.deleteAuthByToken(authToken);


        ResponseException thrown = Assertions.assertThrows(ResponseException.class, gameService::addGameData);
        Assertions.assertEquals(401, thrown.statusCode(), "Should return 401");

    }

}
