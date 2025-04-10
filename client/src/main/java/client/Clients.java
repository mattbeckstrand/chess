package client;

import chess.ChessGame;

public interface Clients {
    String eval(String input);
    String help();
    boolean isLoggedIn();
    boolean inGame();
    default ChessGame.TeamColor getTeamColor(){
        return null;
    }
}
