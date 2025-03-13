package dataaccess.user;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.GameData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SqlUserDAO implements UserDAO{
    public void addUser(UserData user) throws DataAccessException{
        String stmt = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";String password = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement prepStmt = conn.prepareStatement(stmt)) {
            prepStmt.setString(1, user.getUsername());
            prepStmt.setString(2, password);
            prepStmt.setString(3, user.getEmail());

            int rowsAffected = prepStmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new DataAccessException("User insertion failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public UserData findUser(String userName) throws DataAccessException{
        String stmt = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement prepStmt = conn.prepareStatement(stmt)) {
            prepStmt.setString(1, userName);
            ResultSet results = prepStmt.executeQuery();
            if (results.next()) {
                return new UserData(userName, results.getString("password"),
                        results.getString("email"));
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return null;
    }


    public void clear() throws DataAccessException {
        String stmt = "DELETE FROM users";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement prepStmt = conn.prepareStatement(stmt)) {
            prepStmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public boolean isEmpty() throws DataAccessException {
        String stmt = "SELECT COUNT(*) FROM users";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement prepStmt = conn.prepareStatement(stmt);
             ResultSet results = prepStmt.executeQuery()) {
            if (results.next()) {
                return results.getInt(1) == 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return true;
    }
}

