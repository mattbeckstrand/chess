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

public class RegisterServiceTests {
    private MemoryAuthDAO authDao;
    private MemoryUserDAO userDao;

    @BeforeEach
    void setUp() {
        authDao = new MemoryAuthDAO();
        userDao = new MemoryUserDAO();
    }

    @Test
    @DisplayName("Registration successful")
    public void testSuccessfulRegistration() throws ResponseException{
        userDao.addUser(new UserData("testKing", "kingoftests12", "test@example.com"));

        UserData request = new UserData("testKing", "kingoftests12", "test@example.com");
        RegisterService service = new RegisterService(authDao, userDao, request);

        AuthData auth = service.addAuth();

        Assertions.assertEquals("testKing", auth.username(), "Usernames don't match");
        Assertions.assertNotNull(auth.authToken(), "Auth Token Null");

    }

    @Test
    @DisplayName("Registration fail")
    public void testFailedRegistration() throws ResponseException{
        userDao.addUser(new UserData("testKing", "kingoftests12", "test@example.com"));

        UserData request = new UserData("testKing", "kingoftests12", "test@example.com");
        RegisterService service = new RegisterService(authDao, userDao, request);


        ResponseException thrown = Assertions.assertThrows(ResponseException.class, service::addUserData);
        Assertions.assertEquals(403, thrown.StatusCode(), "Should return 403");
    }


}
