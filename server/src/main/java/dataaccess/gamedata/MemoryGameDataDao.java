package dataaccess.gamedata;

import chess.ChessGame;
import chess.ChessPiece;
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
    public void addPlayer(int gameID, String username, String tColor){
        GameData game = gameDataList.get(gameID);
        if (game != null){
            if (tColor.equals("WHITE")){
                game.setWhiteUsername(username);
            } else {
                game.setBlackUsername(username);
            }
        game.setWhiteUsername(username);
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
