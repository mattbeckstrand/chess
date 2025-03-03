package dataaccess.GameData;

import chess.ChessGame;
import model.GameData;
import java.util.HashMap;

public class MemoryGameDataDao implements GameDataDAO{

    final private HashMap<Integer, GameData> gameDataList = new HashMap<>();
    private int nextID = 1;

    @Override
    public GameData createGame(String gameName)  {
        GameData game = new GameData(nextID++, null,null, gameName, new ChessGame());
        gameDataList.put(game.getGameID(), game);
        return game;
    }
    @Override
    public GameData getGame(int gameID) {
        return gameDataList.get(gameID);
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
        if (game != null){
        game.setWhiteUsername(username);
        gameDataList.put(gameID, game);
        }
    }
    @Override
    public void addBlackPlayer(int gameID, String username){
        GameData game = gameDataList.get(gameID);
        if (game != null){
            game.setBlackUsername(username);
            gameDataList.put(gameID, game);
        }
    }

    @Override
    public void clear() {
        gameDataList.clear();
    }

    @Override
    public boolean isEmpty() {
        return gameDataList.isEmpty();
    }


}
