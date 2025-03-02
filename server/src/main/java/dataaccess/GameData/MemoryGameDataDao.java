package dataaccess.GameData;

import chess.ChessGame;
import dataaccess.DataAccessException;
import exception.ResponseException;
import jdk.jfr.Percentage;
import model.GameData;

import javax.xml.crypto.Data;
import java.util.HashMap;
import java.util.Collection;

public class MemoryGameDataDao implements GameDataDAO{

    final private HashMap<Integer, GameData> gameDataList = new HashMap<>();
    private int nextId = 1;

    @Override
    public GameData createGame(String gameName)  {
        GameData game = new GameData(nextId++, null,null, gameName, new ChessGame());
        gameDataList.put(game.getGameID(), game);
        return game;
    }
    @Override
    public GameData getGame(int gameId) {
        return gameDataList.get(gameId);
    }
    @Override
    public HashMap<Integer, GameData> listGames(){
        return gameDataList;
    }
    @Override
    public void updateGame(int gameID){

    }

    @Override
    public void addWhitePlayer(int gameID, String username){
        GameData game = gameDataList.get(gameID);
        game.setWhiteUsername(username);
        }
    @Override
    public void addBlackPlayer(int gameID, String username){
        GameData game = gameDataList.get(gameID);
        game.setBlackUsername(username);
    }

    @Override
    public void clear() {
        gameDataList.clear();
    }

}
