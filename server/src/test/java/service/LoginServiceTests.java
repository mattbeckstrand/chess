package service;

import dataaccess.Auth.MemoryAuthDAO;
import dataaccess.User.MemoryUserDAO;
import exception.ResponseException;
import model.AuthData;
import model.LoginRequest;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class LoginServiceTests {
    private MemoryAuthDAO authDao;
    private MemoryUserDAO userDao;


    @BeforeEach
    void setUp() {
        authDao = new MemoryAuthDAO();
        userDao = new MemoryUserDAO();
    }
    @Test
    @DisplayName("Login successful")
    public void testSuccessfulLogin() throws ResponseException{
        userDao.addUser(new UserData("testKing", "kingoftests12", "test@example.com"));

        LoginRequest request = new LoginRequest("testKing", "kingoftests12");
        LoginService service = new LoginService(authDao, userDao, request);

        AuthData auth = service.addAuth();

        Assertions.assertEquals("testKing", auth.username(), "Usernames don't match");
        Assertions.assertNotNull(auth.authToken(), "Auth Token Null");
    }

    @Test
    @DisplayName("Wrong password")
    public void testWrongPassword() throws ResponseException{
        userDao.addUser(new UserData("testKing", "kingoftests12", "test@example.com"));

        LoginRequest request = new LoginRequest("testKing", "kingoftests1");
        LoginService service = new LoginService(authDao, userDao, request);

        ResponseException thrown = Assertions.assertThrows(ResponseException.class, service::checkUserPassword);
        Assertions.assertEquals(401, thrown.StatusCode(), "Should return 401");



    }

}
