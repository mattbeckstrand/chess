package dataaccess.auth;

import com.mysql.cj.protocol.Resultset;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.AuthData;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SqlAuthDao implements AuthDAO {

    @Override
    public void addAuth(AuthData auth) throws DataAccessException {
        String stmt = "INSERT INTO auth (authToken, username) VALUES (?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement prepStmt = conn.prepareStatement(stmt)) {
            prepStmt.setString(1, auth.authToken());
            prepStmt.setString(2, auth.username());
            prepStmt.executeUpdate();
            System.out.println("auth Added");
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public AuthData findAuthByToken(String authToken) throws DataAccessException {
        String stmt = "SELECT * FROM auth WHERE authToken = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement prepStmt = conn.prepareStatement(stmt)) {
            prepStmt.setString(1, authToken);
            ResultSet results = prepStmt.executeQuery();
            if (results.next()) {
                System.out.println(results.getString("username"));
                return new AuthData(results.getString("username"), authToken);
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return null;
    }

    @Override
    public void deleteAuthByToken(String authToken) throws DataAccessException {
        String stmt = "DELETE FROM auth WHERE authToken = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement prepStmt = conn.prepareStatement(stmt)) {
            prepStmt.setString(1, authToken);
            prepStmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public String generateToken(){
        return UUID.randomUUID().toString();
    }

    @Override
    public void clear() throws DataAccessException {
        String stmt = "DELETE FROM auth";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement prepStmt = conn.prepareStatement(stmt)) {
            prepStmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public boolean isEmpty() throws DataAccessException {
        String sql = "SELECT COUNT(*) FROM auth";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement prepStmt = conn.prepareStatement(sql);
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