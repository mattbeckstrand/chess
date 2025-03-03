package service;

import dataaccess.auth.MemoryAuthDAO;
import dataaccess.gameData.MemoryGameDataDao;
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


public class JoinGameServiceTests {
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
    @DisplayName("Join game Successful")
    public void testJoinGame() throws ResponseException{
        userDao.addUser(new UserData("testKing", "kingoftests12", "test@example.com"));

        LoginRequest request = new LoginRequest("testKing", "kingoftests12");
        LoginService logService = new LoginService(authDao, userDao, request);

        AuthData auth = logService.addAuth();
        String authToken = auth.authToken();

        CreateGameService gameService = new CreateGameService(gameDataDao, authToken, authDao, "gameName");
        gameService.addGameData();

        JoinGameService joinGame = new JoinGameService(gameDataDao, authToken, authDao, 1, "WHITE");
        GameData joinedGameData = joinGame.joinGame();
        Assertions.assertEquals("testKing", joinedGameData.getWhiteUsername(), "White Username doesn't match");
    }

    @Test
    @DisplayName("Wrong color join game")
    public void testWrongColor() throws ResponseException{
        userDao.addUser(new UserData("testKing", "kingoftests12", "test@example.com"));

        LoginRequest request = new LoginRequest("testKing", "kingoftests12");
        LoginService logService = new LoginService(authDao, userDao, request);

        AuthData auth = logService.addAuth();
        String authToken = auth.authToken();

        CreateGameService gameService = new CreateGameService(gameDataDao, authToken, authDao, "gameName");
        gameService.addGameData();

        JoinGameService joinGameServ= new JoinGameService(gameDataDao, authToken, authDao, 1, "BLUE");

        ResponseException thrown = Assertions.assertThrows(ResponseException.class, joinGameServ::joinGame);
        Assertions.assertEquals(400, thrown.statusCode(), "Should return 400");

    }

}
