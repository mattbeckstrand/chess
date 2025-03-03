package service;

import dataaccess.auth.MemoryAuthDAO;
import dataaccess.user.MemoryUserDAO;
import exception.ResponseException;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class LogoutServiceTests {
    private MemoryAuthDAO authDao;
    private MemoryUserDAO userDao;


    @BeforeEach
    void setUp() {
        authDao = new MemoryAuthDAO();
        userDao = new MemoryUserDAO();
    }
    @Test
    @DisplayName("Logout successful")
    public void testSuccessfulLogout() throws ResponseException{
        userDao.addUser(new UserData("testKing", "kingoftests12", "test@example.com"));

        UserData request = new UserData("testKing", "kingoftests12", "test@example.com");
        RegisterService regService = new RegisterService(authDao, userDao, request);

        AuthData auth = regService.addAuth();
        String authToken = auth.authToken();

        LogoutService serv = new LogoutService(authDao,authToken);
        serv.deleteAuth();


        ResponseException thrown = Assertions.assertThrows(ResponseException.class, serv::checkAuth);
        Assertions.assertEquals(401, thrown.statusCode(), "Should return 401");
    }

    @Test
    @DisplayName("Not logged in")
    public void testThrowError() throws ResponseException{

        UserData request = new UserData("testKing", "kingoftests12", "test@example.com");
        RegisterService regService = new RegisterService(authDao, userDao, request);

        AuthData auth = regService.addAuth();
        String authToken = auth.authToken();

        LogoutService serv = new LogoutService(authDao,authToken);
        serv.deleteAuth();


        ResponseException thrown = Assertions.assertThrows(ResponseException.class, serv::checkAuth);
        Assertions.assertEquals(401, thrown.statusCode(), "Should return 401");


    }

}