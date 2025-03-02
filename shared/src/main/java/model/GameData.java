package model;
import chess.ChessGame;

public class GameData {
    private final int gameID;
    private String whiteUsername;
    private String blackUsername;
    private final String gameName;
    private final ChessGame game;

    public GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game){
        this.gameID = gameID;
        this.whiteUsername = whiteUsername;
        this.blackUsername = blackUsername;
        this.gameName = gameName;
        this.game = game;
    }

    public String getWhiteUsername() { return whiteUsername; }

    public int getGameID() { return gameID; }

    public String getBlackUsername() { return blackUsername; }

    public String getGameName() { return gameName; }

    public ChessGame getGame() { return game; }

    public void setWhiteUsername(String whiteUsername){
        this.whiteUsername = whiteUsername;
    }

    public void setBlackUsername(String blackUsername) {
        this.blackUsername = blackUsername;
    }
}
