package database;

import dataaccess.DataAccessException;
import dataaccess.auth.SqlAuthDao;
import dataaccess.DatabaseManager;
import model.AuthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AuthDaoTests {
    private SqlAuthDao authDao;

    @BeforeEach
    void setUp() throws DataAccessException {
        this.authDao = new SqlAuthDao();
        DatabaseManager.createDatabase();
        authDao.clear();
    }

    @Test
    @DisplayName("Add Auth Success")
    public void testAddAuth() throws DataAccessException {
        String authToken = authDao.generateToken();
        AuthData testAuth = new AuthData("testKing", authToken);
        authDao.addAuth(testAuth);

        AuthData retrievedAuth = authDao.findAuthByToken(authToken);
        assertNotNull(retrievedAuth, "Auth should exist after being added.");
        assertEquals("testKing", retrievedAuth.username(), "Auth username should match.");
        assertEquals(authToken, retrievedAuth.authToken(), "Auth token should match.");
    }

    @Test
    @DisplayName("Duplicate Auth Token")
    public void testDuplicateAuthError() throws DataAccessException {
        String authToken = authDao.generateToken();
        AuthData testAuth = new AuthData("testKing", authToken);
        authDao.addAuth(testAuth);

        DataAccessException exception = assertThrows(DataAccessException.class, () -> authDao.addAuth(testAuth));
        assertTrue(exception.getMessage().contains("Duplicate entry"), "Exception should indicate a duplicate entry.");
    }

    @Test
    @DisplayName("Find Auth by Token Success")
    public void testFindAuthByToken() throws DataAccessException {
        String authToken = authDao.generateToken();
        AuthData testAuth = new AuthData("testKing", authToken);
        authDao.addAuth(testAuth);

        AuthData retrievedAuth = authDao.findAuthByToken(authToken);
        assertNotNull(retrievedAuth, "Auth should exist.");
        assertEquals("testKing", retrievedAuth.username(), "Username should match.");
    }

    @Test
    @DisplayName("Find Auth by Invalid Token")
    public void testFindAuthByInvalidToken() throws DataAccessException {
        authDao.clear();
        AuthData retrievedAuth = authDao.findAuthByToken("invalidToken");
        assertNull(retrievedAuth, "Finding a non-existent auth token should return null.");
    }

    @Test
    @DisplayName("Delete Auth by Token")
    public void testDeleteAuthByToken() throws DataAccessException {
        String authToken = authDao.generateToken();
        AuthData testAuth = new AuthData("testKing", authToken);
        authDao.addAuth(testAuth);
        authDao.deleteAuthByToken(authToken);

        AuthData retrievedAuth = authDao.findAuthByToken(authToken);
        assertNull(retrievedAuth, "Auth should be deleted and return null.");
    }

    @Test
    @DisplayName("Test Clear Function")
    public void testClear() throws DataAccessException {
        String authToken = authDao.generateToken();
        AuthData testAuth = new AuthData("testKing", authToken);
        authDao.addAuth(testAuth);
        authDao.clear();

        assertTrue(authDao.isEmpty(), "Database should be empty after clearing.");
    }

    @Test
    @DisplayName("Test isEmpty Function")
    public void testIsEmpty() throws DataAccessException {
        authDao.clear();
        assertTrue(authDao.isEmpty(), "Database should be empty when no auth entries exist.");

        String authToken = authDao.generateToken();
        AuthData testAuth = new AuthData("testKing", authToken);
        authDao.addAuth(testAuth);

        assertFalse(authDao.isEmpty(), "Database should not be empty after adding an auth entry.");
    }
}
