package database;

import dataaccess.DataAccessException;
import dataaccess.auth.SqlAuthDao;
import dataaccess.gamedata.SqlGameDataDAO;
import dataaccess.user.SqlUserDAO;
import dataaccess.DatabaseManager;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;


public class UserDaoTests {
    private SqlUserDAO userDao;

    @BeforeEach
    void setUp() throws DataAccessException {
        this.userDao = new SqlUserDAO();
        DatabaseManager.createDatabase();
    }

    @Test
    @DisplayName("Add User Success")
    public void testAddUser() throws DataAccessException {
        userDao.clear();
        UserData testUser = new UserData("testKing", "kingoftests12", "test@example.com");
        userDao.addUser(testUser);

        String stmt = "SELECT COUNT(*) FROM users WHERE username = ?";
        int userCount = 0;

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement prepStmt = conn.prepareStatement(stmt)) {
            prepStmt.setString(1, testUser.getUsername());
            ResultSet results = prepStmt.executeQuery();
            if (results.next()) {
                userCount = results.getInt(1);
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }

        assertEquals(1, userCount, "User should have been added exactly once.");
    }


    @Test
    @DisplayName("Duplicate Users")
    public void testDuplicateUserError() throws DataAccessException {
        userDao.clear();
        UserData user1 = new UserData("testKing", "kingoftests12", "test@example.com");
        userDao.addUser(user1);

        DataAccessException exception = assertThrows(DataAccessException.class, () -> userDao.addUser(user1));

        assertTrue(exception.getMessage().contains("Duplicate entry"),
                "Exception should indicate a duplicate entry.");

    }

    @Test
    @DisplayName("Find User Success")
    public void testFindUser() throws DataAccessException {
        userDao.clear();
        UserData testUser = new UserData("testKing", "kingoftests12", "test@example.com");
        userDao.addUser(testUser);
        String stmt = "SELECT COUNT(*) FROM users WHERE username = ?";
        int userCount = 0;

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement prepStmt = conn.prepareStatement(stmt)) {
            prepStmt.setString(1, testUser.getUsername());
            ResultSet results = prepStmt.executeQuery();
            if (results.next()) {
                userCount = results.getInt(1);
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }

        assertEquals(1, userCount, "User should have been added exactly once.");
    }


    @Test
    @DisplayName("Find User that didn't exist")
    public void testFindUserError() throws DataAccessException {
        userDao.clear();
        UserData user1 = new UserData("testKing", "kingoftests12", "test@example.com");
        userDao.addUser(user1);
        UserData result = userDao.findUser("testKing1");
        assertNull(result, "Finding a non-existent user should return null.");
    }

    @Test
    @DisplayName("Test the clear function")
    public void testClear() throws DataAccessException {
        userDao.clear();
        UserData testUser = new UserData("testKing", "kingoftests12", "test@example.com");
        userDao.addUser(testUser);
        userDao.clear();

        String stmt = "SELECT COUNT(*) FROM users";
        int userCount = 0;

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement prepStmt = conn.prepareStatement(stmt);
             ResultSet results = prepStmt.executeQuery()) {
            if (results.next()) {
                userCount = results.getInt(1);
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }

        assertEquals(0, userCount, "User count should be 0 after clearing.");
    }

    @Test
    @DisplayName("Test the empty function")
    public void testIsEmpty() throws DataAccessException {
        userDao.clear();
        UserData testUser = new UserData("testKing", "kingoftests12", "test@example.com");
        userDao.addUser(testUser);
        userDao.clear();

        assertTrue(userDao.isEmpty(), "Database should be empty ");
    }

}
