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
        serverFacade.Delete();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    public AuthData registerTestUser() throws ResponseException{
        UserData userData = new UserData("test", "test", "test");
        return serverFacade.Register(userData);
    }


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

    @Test
    public void registerTest() throws ResponseException {
        AuthData authData = registerTestUser();
        String username = authData.username();
        Assertions.assertEquals("test", username);
    }

    @Test
    public void registerTestFailed() throws ResponseException {
        String uniqueUsername = "test" + System.currentTimeMillis(); // Ensures uniqueness
        UserData userData = new UserData(uniqueUsername, "test", "test");
        serverFacade.Register(userData);
        ResponseException exception = assertThrows(ResponseException.class, () -> serverFacade.Register(userData));
        assertTrue(exception.getMessage().contains("already taken"), "Exception should indicate a duplicate registration.");

    }
    @Test
    public void loginTest() throws ResponseException{
        registerTestUser();
        LoginRequest loginRequest = new LoginRequest("test", "test");
        String authToken = serverFacade.Login(loginRequest);
        Assertions.assertNotNull(authToken);
    }

    @Test
    public void failedLoginTest() throws ResponseException{
        registerTestUser();
        LoginRequest loginRequest = new LoginRequest("test", "test1");
        ResponseException exception = assertThrows(ResponseException.class, () -> serverFacade.Login(loginRequest));
        assertTrue(exception != null);
    }

    @Test
    public void logoutTest() throws ResponseException{
        AuthData authData = registerTestUser();
        serverFacade.Logout(authData.authToken());
        CreateGameRequest request = new CreateGameRequest("gameName");
        ResponseException exception = assertThrows(ResponseException.class, () -> serverFacade.CreateGame(request, authData.authToken()));
        assertNotNull(exception);
    }
    @Test
    public void failedLogoutTest() throws ResponseException{
        AuthData authData = registerTestUser();
        serverFacade.Logout(authData.authToken());
        ResponseException exception = assertThrows(ResponseException.class, () -> serverFacade.Logout(authData.authToken()));
        assertNotNull(exception);
    }

    public int createGame(AuthData authData) throws ResponseException{
        CreateGameRequest request = new CreateGameRequest("gameName");
        int gameId = serverFacade.CreateGame(request, authData.authToken());
        return gameId;
    }
    @Test
    public void createGameTest() throws ResponseException{
        AuthData authData = registerTestUser();
        int gameId = createGame(authData);
        assertNotNull(gameId);
    }

    @Test
    public void createGameTestFailed() throws ResponseException{
        AuthData authData = registerTestUser();
        serverFacade.Logout(authData.authToken());
        CreateGameRequest request = new CreateGameRequest("gameName");
        ResponseException exception = assertThrows(ResponseException.class, () -> serverFacade.CreateGame(request, authData.authToken()));
        assertNotNull(exception);
    }

    @Test
    public void joinGame() throws ResponseException{
        AuthData authData = registerTestUser();
        int gameId = createGame(authData);
        System.out.println(gameId);
        JoinGameRequest request = new JoinGameRequest("WHITE", gameId);
        serverFacade.JoinGame(authData.authToken(), request);
    }

    @Test
    public void failJoinGame() throws ResponseException{
        AuthData authData = registerTestUser();
        int gameId = createGame(authData);
        System.out.println(gameId);
        JoinGameRequest request = new JoinGameRequest("WHITE", gameId);
        serverFacade.JoinGame(authData.authToken(), request);
        ResponseException exception = assertThrows(ResponseException.class, () -> serverFacade.JoinGame(authData.authToken(), request));
        assertNotNull(exception);
    }

}

