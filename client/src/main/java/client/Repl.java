package client;


import chess.ChessGame;
import client.websocket.NotificationHandler;
import client.websocket.WebSocketFacade;
import exception.ResponseException;
import ui.DrawingChessBoard;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.Scanner;

import static java.awt.Color.GREEN;
import static java.awt.Color.RED;
import static ui.EscapeSequences.*;

public class Repl implements NotificationHandler {
    private Clients client;
    private String authToken;

    public Repl(String serverUrl) {
        this.client = new UnloggedInClient(serverUrl, this);

    }

    public void setClient(Clients newClient){
        this.client = newClient;
    }

    public void setClientToGame(String serverUrl, String authToken, int gameId, ChessGame.TeamColor teamColor) throws IOException {
        WebSocketFacade ws;
        try {
            ws = new WebSocketFacade(serverUrl, this);
        } catch (ResponseException e) {
            System.out.println("Failed to start WebSocket: " + e.getMessage());
            return;
        }
        this.client = new InGameClient(serverUrl, authToken, this, this, gameId, ws, teamColor);
    }

    public void run() {
        System.out.println("Welcome to 240 chess. Type Help to get started.");
        System.out.print(client.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                System.out.print(result);

                if (result.contains("Successfully logged in\n") || result.contains("Successfully logged out\n")){
                    System.out.println(client.help());
                }
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    public void notify(ServerMessage message) {
        switch (message.getServerMessageType()) {
            case NOTIFICATION -> {
                NotificationMessage notif = (NotificationMessage) message;
                System.out.println(SET_TEXT_COLOR_RED + notif.getNotification() + RESET_TEXT_COLOR);
            }
            case ERROR -> {
                ErrorMessage err = (ErrorMessage) message;
                System.out.println(SET_TEXT_COLOR_RED + "ERROR: " + err.getError() + RESET_TEXT_COLOR);
            }
            case LOAD_GAME -> {
                LoadGameMessage gameMsg = (LoadGameMessage) message;
                ChessGame game = gameMsg.getGame();
                DrawingChessBoard.drawChessBoard(System.out, game, client.getTeamColor(), null);
            }
        }
        printPrompt();
    }

    private void printPrompt() {
        String status = client.isLoggedIn() ? (client.inGame() ?
                SET_TEXT_COLOR_BLUE + "[IN_GAME]" : SET_TEXT_COLOR_BLUE + "[LOGGED_IN]") : SET_TEXT_COLOR_MAGENTA + "[LOGGED_OUT]";
        System.out.print("\n" + RESET_TEXT_COLOR + status + RESET_TEXT_COLOR + " >>> " + SET_TEXT_COLOR_GREEN + "\n");
    }

}