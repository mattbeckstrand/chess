package dataaccess.gamedata;

import chess.ChessGame;
import chess.ChessPiece;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.GameData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class SqlGameDataDAO implements GameDataDAO {
    private int nextID = 1;

    public GameData createGame(String gameName) throws DataAccessException {
        String stmt = "INSERT INTO games (gameID, whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?, ?)";
        ChessGame game = new ChessGame();
        Gson gson = new Gson();
        String jsonGame = gson.toJson(game);
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement prepStmt = conn.prepareStatement(stmt)) {
            prepStmt.setInt(1, nextID);
            prepStmt.setString(2, null);
            prepStmt.setString(3, null);
            prepStmt.setString(4, gameName);
            prepStmt.setString(5, jsonGame);
            prepStmt.executeUpdate();
            System.out.println("game Added");
            GameData data = new GameData(nextID, null, null, gameName, game);
            nextID += 1;
            return data;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public GameData getGame(int gameId) throws DataAccessException {
        String stmt = "SELECT * FROM games WHERE gameID = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement prepStmt = conn.prepareStatement(stmt)) {
            prepStmt.setInt(1, gameId);
            ResultSet results = prepStmt.executeQuery();
            if (results.next()) {
                String jsonGame = results.getString("game");
                ChessGame game = new Gson().fromJson(jsonGame, ChessGame.class);
                return new GameData(results.getInt("gameID"), results.getString("whiteUsername"),
                        results.getString("blackUsername"), results.getString("gameName"), game);
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return null;
    }

    ;

    public HashMap<Integer, GameData> listGames() throws DataAccessException {
        HashMap<Integer, GameData> games = new HashMap<>();
        String stmt = "SELECT * FROM games";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement prepStmt = conn.prepareStatement(stmt);
             ResultSet results = prepStmt.executeQuery()) {
            while (results.next()) {
                String jsonGame = results.getString("game");
                ChessGame game = new Gson().fromJson(jsonGame, ChessGame.class);
                GameData data = new GameData(results.getInt("gameID"), results.getString("whiteUsername"),
                        results.getString("blackUsername"), results.getString("gameName"), game);
                games.put(data.getGameID(), data);
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return games;
    }
    @Override
    public void addPlayer(int gameId, String username, String tColor) throws DataAccessException {
        String stmt;
        if (tColor.equals("BLACK")) {
            stmt = "UPDATE games SET blackUsername = ? WHERE gameID = ?";
        } else {
            stmt = "UPDATE games SET whiteUsername = ? WHERE gameID = ?";
        }
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement prepStmt = conn.prepareStatement(stmt)) {
            prepStmt.setString(1, username);
            prepStmt.setInt(2, gameId);
            int affectedRows = prepStmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DataAccessException("game not found or player already assigned.");
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }


    public void clear() throws DataAccessException {
        String stmt = "DELETE FROM games";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement prepStmt = conn.prepareStatement(stmt)) {
            prepStmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public boolean isEmpty() throws DataAccessException {
        String stmt = "SELECT COUNT(*) FROM games";
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
