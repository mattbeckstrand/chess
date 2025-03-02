package model;

public record GameSummary(int gameID, String gameName, String whiteUsername, String blackUsername) {
    public GameSummary(int gameID, String gameName, String whiteUsername, String blackUsername) {
        this.gameID = gameID;
        this.gameName = gameName;
        this.whiteUsername = (whiteUsername != null) ? whiteUsername : "";
        this.blackUsername = (blackUsername != null) ? blackUsername : "";
    }
}

