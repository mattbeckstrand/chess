package service;

import com.google.gson.Gson;
import dataaccess.Auth.MemoryAuthDAO;
import dataaccess.GameData.MemoryGameDataDao;
import dataaccess.User.MemoryUserDAO;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.LoginRequest;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class ListGameServiceTest {
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
    @DisplayName("List games successful")
    public void testListGames() throws ResponseException {
        Gson gson = new Gson();
        userDao.addUser(new UserData("testKing", "kingoftests12", "test@example.com"));

        LoginRequest request = new LoginRequest("testKing", "kingoftests12");
        LoginService logService = new LoginService(authDao, userDao, request);

        AuthData auth = logService.addAuth();
        String authToken = auth.authToken();

        CreateGameService gameService = new CreateGameService(gameDataDao, authToken, authDao, "gameName");
        gameService.addGameData();
        gameService.addGameData();
        gameService.addGameData();

        ListGamesService listService = new ListGamesService(authToken, authDao, gameDataDao);

        Assertions.assertEquals("[{\"gameID\":1,\"gameName\":\"gameName\"},{\"gameID\":2,\"gameName\":\"gameName\"},{\"gameID\":3,\"gameName\":\"gameName\"}]", gson.toJson(listService.listGameData()), "List is not the same");

    }

    @Test
    @DisplayName("Unauthorized list games")
    public void testUnauthorizedListGames() throws ResponseException{
        userDao.addUser(new UserData("testKing", "kingoftests12", "test@example.com"));

        LoginRequest request = new LoginRequest("testKing", "kingoftests12");
        LoginService logService = new LoginService(authDao, userDao, request);

        AuthData auth = logService.addAuth();
        String authToken = auth.authToken();

        CreateGameService gameService = new CreateGameService(gameDataDao, authToken, authDao, "gameName");
        gameService.addGameData();
        gameService.addGameData();
        gameService.addGameData();

        ListGamesService listService = new ListGamesService(authToken, authDao, gameDataDao);
        authDao.deleteAuthByToken(authToken);

        ResponseException thrown = Assertions.assertThrows(ResponseException.class, listService::listGameData);
        Assertions.assertEquals(401, thrown.StatusCode(), "Should return 401 unauthorized");

    }

}
