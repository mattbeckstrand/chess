package service;

import dataaccess.auth.MemoryAuthDAO;
import dataaccess.gamedata.MemoryGameDataDao;
import dataaccess.user.MemoryUserDAO;
import exception.ResponseException;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class DeleteServiceTests {
    private MemoryAuthDAO authDao;
    private MemoryUserDAO userDao;
    private MemoryGameDataDao gameDataDao;

    @BeforeEach
    void setUp() {
        authDao = new MemoryAuthDAO();
        userDao = new MemoryUserDAO();
        gameDataDao = new MemoryGameDataDao();
    }
    @Test
    @DisplayName("Delete successful")
    public void testSuccessfulDeletion() throws ResponseException {
        userDao.addUser(new UserData("testKing", "kingoftests12", "test@example.com"));

        UserData request = new UserData("testKing", "kingoftests12", "test@example.com");
        RegisterService regService = new RegisterService(authDao, userDao, request);

        AuthData auth = regService.addAuth();
        String authToken = auth.authToken();

        CreateGameService gameService = new CreateGameService(gameDataDao, authToken, authDao, "gameName");
        gameService.addGameData();

        DeleteService dService = new DeleteService(gameDataDao, authDao, userDao);

        dService.delete();

        Assertions.assertTrue(authDao.isEmpty(), "Auth Dao isn't empty");
        Assertions.assertTrue(gameDataDao.isEmpty(), "Game Dao isn't empty");
        Assertions.assertTrue(userDao.isEmpty(), "User Dao isn't empty");
    }

}