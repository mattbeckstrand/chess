package database;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.gamedata.SqlGameDataDAO;
import dataaccess.DatabaseManager;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class GameDataDaoTests {
    private SqlGameDataDAO gameDao;

    @BeforeEach
    void setUp() throws DataAccessException {
        gameDao = new SqlGameDataDAO();
        DatabaseManager.createDatabase();
        gameDao.clear();
    }

    @Test
    @DisplayName("Create Game Success")
    public void testCreateGameSuccess() throws DataAccessException {
        GameData game = gameDao.createGame("gameTest");
        assertNotNull(game, "game should be there");
        assertEquals("gameTest", game.getGameName(), "names should be the same");
    }

    @Test
    @DisplayName("get game thats not there")
    public void testGetGameFailure() throws DataAccessException {
        GameData game = gameDao.getGame(38);
        assertNull(game, "should return null");
    }

    @Test
    @DisplayName("get games")
    public void testGetGame() throws DataAccessException {
        GameData createdGame = gameDao.createGame("mattbeck250");
        GameData retrievedGame = gameDao.getGame(createdGame.getGameID());
        assertNotNull(retrievedGame, "game should be returned");
        assertEquals("mattbeck250", retrievedGame.getGameName(), "game names should match");
    }

    @Test
    @DisplayName("list games")
    public void testListGames() throws DataAccessException {
        gameDao.createGame("whiteGame");
        gameDao.createGame("blackGame");
        HashMap<Integer, GameData> games = gameDao.listGames();
        assertEquals(2, games.size(), "should list 2");
    }

    @Test
    @DisplayName("empty list games")
    public void testListGamesFailure() throws DataAccessException {
        HashMap<Integer, GameData> games = gameDao.listGames();
        assertTrue(games.isEmpty(), "if no games, empty list should return");
    }

    @Test
    @DisplayName("add player")
    public void testAddPlayerSuccess() throws DataAccessException {
        GameData game = gameDao.createGame("Magnus");
        gameDao.addPlayer(game.getGameID(), "MagnusWins", "WHITE");
        GameData updatedGame = gameDao.getGame(game.getGameID());
        assertEquals("MagnusWins", updatedGame.getWhiteUsername(), "white player should be there");
    }

    @Test
    @DisplayName("add player to no game")
    public void testAddPlayerFailure() throws DataAccessException {
        DataAccessException exception = assertThrows(DataAccessException.class, () -> {
            gameDao.addPlayer(1, "test1username", "WHITE");
        });
        assertTrue(exception.getMessage().contains("game not found"), "should fail here");
    }

    @Test
    @DisplayName("clear Games")
    public void testClear() throws DataAccessException {
        gameDao.createGame( "coding123");
        gameDao.createGame("win20");
        gameDao.clear();
        assertTrue(gameDao.isEmpty(), "database should be empty");
    }

    @Test
    @DisplayName("starting is empty")
    public void testIsEmpty() throws DataAccessException {
        assertTrue(gameDao.isEmpty(), "database should be empty");
    }
}
