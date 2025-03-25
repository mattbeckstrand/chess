package client;

import exception.ResponseException;
import model.*;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;

    @BeforeAll
    public static void init() throws ResponseException{
        server = new Server();
        var port = server.run(8000);
        System.out.println("Started test HTTP server on " + port);
        serverFacade = new ServerFacade("http://localhost:8000");
        serverFacade.delete();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    public AuthData registerTestUser() throws ResponseException{
        UserData userData = new UserData("test", "test", "test");
        return serverFacade.register(userData);
    }


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

    @Test
    public void registerTest() throws ResponseException {
        serverFacade.delete();
        AuthData authData = registerTestUser();
        String username = authData.username();
        Assertions.assertEquals("test", username);
    }

    @Test
    public void registerTestFailed() throws ResponseException {
        serverFacade.delete();
        String uniqueUsername = "test" + System.currentTimeMillis(); // Ensures uniqueness
        UserData userData = new UserData(uniqueUsername, "test", "test");
        serverFacade.register(userData);
        ResponseException exception = assertThrows(ResponseException.class, () -> serverFacade.register(userData));
        assertTrue(exception.getMessage().contains("already taken"), "Exception should indicate a duplicate registration.");

    }
    @Test
    public void loginTest() throws ResponseException{
        serverFacade.delete();
        registerTestUser();
        LoginRequest loginRequest = new LoginRequest("test", "test");
        String authToken = serverFacade.login(loginRequest);
        Assertions.assertNotNull(authToken);
    }

    @Test
    public void failedLoginTest() throws ResponseException{
        serverFacade.delete();
        registerTestUser();
        LoginRequest loginRequest = new LoginRequest("test", "test1");
        ResponseException exception = assertThrows(ResponseException.class, () -> serverFacade.login(loginRequest));
        assertTrue(exception != null);
    }

    @Test
    public void logoutTest() throws ResponseException{
        serverFacade.delete();
        AuthData authData = registerTestUser();
        CreateGameRequest request = new CreateGameRequest("gameName");
        int gameId = serverFacade.createGame(request, authData.authToken());
        serverFacade.logout(authData.authToken());
        JoinGameRequest requestGame = new JoinGameRequest("WHITE", gameId);
        ResponseException exception = assertThrows(ResponseException.class, () -> serverFacade.joinGame(authData.authToken(), requestGame));
        assertNotNull(exception);
    }
    @Test
    public void failedLogoutTest() throws ResponseException{
        serverFacade.delete();
        AuthData authData = registerTestUser();
        serverFacade.logout(authData.authToken());
        ResponseException exception = assertThrows(ResponseException.class, () -> serverFacade.logout(authData.authToken()));
        assertNotNull(exception);
    }

    public int createGame(AuthData authData) throws ResponseException{
        CreateGameRequest request = new CreateGameRequest("gameName");
        int gameId = serverFacade.createGame(request, authData.authToken());
        return gameId;
    }
    @Test
    public void createGameTest() throws ResponseException{
        serverFacade.delete();
        AuthData authData = registerTestUser();
        int gameId = createGame(authData);
        assertNotNull(gameId);
    }

    @Test
    public void createGameTestFailed() throws ResponseException{
        serverFacade.delete();
        AuthData authData = registerTestUser();
        serverFacade.logout(authData.authToken());
        CreateGameRequest request = new CreateGameRequest("gameName");
        ResponseException exception = assertThrows(ResponseException.class, () -> serverFacade.createGame(request, authData.authToken()));
        assertNotNull(exception);
    }

    @Test
    public void joinGameTests() throws ResponseException{
        serverFacade.delete();
        assertDoesNotThrow(() -> {
            AuthData authData = registerTestUser();
            int gameId = createGame(authData);
            JoinGameRequest request = new JoinGameRequest("WHITE", gameId);
            serverFacade.joinGame(authData.authToken(), request);
        });
    }


    @Test
    public void failJoinGameTests() throws ResponseException{
        serverFacade.delete();
        AuthData authData = registerTestUser();
        int gameId = createGame(authData);
        serverFacade.logout(authData.authToken());
        JoinGameRequest request = new JoinGameRequest("WHITE", gameId);
        ResponseException exception = assertThrows(ResponseException.class, () -> serverFacade.joinGame(authData.authToken(), request));
        assertNotNull(exception);
    }

    @Test
    public void listGamesTest() throws ResponseException{
        serverFacade.delete();
        AuthData authData = registerTestUser();
        createGame(authData);
        createGame(authData);
        createGame(authData);
        ListGamesResponse response = serverFacade.listGames(authData.authToken());
        assertNotNull(response);
    }

    @Test
    public void failListGamesTest() throws ResponseException{
        serverFacade.delete();
        AuthData authData = registerTestUser();
        ListGamesResponse response = serverFacade.listGames(authData.authToken());
        assertTrue(response.games().isEmpty());
    }
    @Test
    public void deleteTest() throws ResponseException{
        serverFacade.delete();
        AuthData authData = registerTestUser();
        serverFacade.delete();
        LoginRequest request = new LoginRequest(authData.username(), authData.authToken());
        ResponseException exception = assertThrows(ResponseException.class, () -> serverFacade.login(request));
        assertNotNull(exception);

    }
}

