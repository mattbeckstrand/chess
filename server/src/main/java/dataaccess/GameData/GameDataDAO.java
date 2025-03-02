package dataaccess.GameData;

import model.GameData;
import chess.ChessGame;
import dataaccess.DataAccessException;

import java.util.Collection;
import java.util.HashMap;

public interface GameDataDAO {
    GameData createGame(String gameName) throws DataAccessException;

    GameData getGame(int gameId) throws DataAccessException;

    HashMap<Integer, GameData> listGames() throws DataAccessException;

    void updateGame(int gameID) throws DataAccessException;

    void addWhitePlayer(int gameId, String username) throws DataAccessException;

    void addBlackPlayer(int gameId, String username) throws  DataAccessException;

    void clear() throws DataAccessException;

}
