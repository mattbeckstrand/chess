package dataaccess.gamedata;

import chess.ChessPiece;
import model.GameData;
import dataaccess.DataAccessException;

import java.util.HashMap;

public interface GameDataDAO {
    GameData createGame(String gameName) throws DataAccessException;

    GameData getGame(int gameId) throws DataAccessException;

    HashMap<Integer, GameData> listGames() throws DataAccessException;

    void addPlayer(int gameId, String username, String tColor) throws DataAccessException;

    void clear() throws DataAccessException;

    boolean isEmpty() throws DataAccessException;

}
