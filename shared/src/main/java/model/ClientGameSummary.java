package model;

public record ClientGameSummary(String gameName, String whiteUsername, String blackUsername) {
    public ClientGameSummary(String gameName, String whiteUsername, String blackUsername) {
        this.gameName = gameName;
        this.whiteUsername = whiteUsername;
        this.blackUsername = blackUsername;
    }
}