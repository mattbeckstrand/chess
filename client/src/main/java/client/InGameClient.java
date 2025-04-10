package client;

import client.websocket.NotificationHandler;
import client.websocket.WebSocketFacade;
import exception.ResponseException;
import model.*;
import server.ServerFacade;
import ui.DrawingChessBoard;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static ui.EscapeSequences.*;

public class InGameClient implements Clients{
    private final ServerFacade server;
    private final String serverUrl;
    private final Repl repl;
    private final String authToken;
    private WebSocketFacade ws;
    private final NotificationHandler notificationHandler;
    private final Integer gameId;

    public InGameClient(String serverUrl, String authToken, Repl repl, NotificationHandler notificationHandler, int gameId, WebSocketFacade ws) throws IOException {
        this.serverUrl = serverUrl;
        this.server = new ServerFacade(serverUrl);
        this.authToken = authToken;
        this.repl = repl;
        this.notificationHandler = notificationHandler;
        this.gameId = gameId;
        this.ws = ws;
        ws.connect(authToken, gameId);
    }

    @Override
    public boolean isLoggedIn() {
        return true;
    }

    public String eval(String input) {
        try {
            var tokens = input.split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "redraw" -> redraw(params);
                case "leave" -> leave();
                case "move" -> move(params);
                case "resign" -> resign(params);
                case "highlight" -> highlightMoves(params);
                default -> help();

            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String redraw(String... params) throws ResponseException {

        return "Successfully created game";
    }

    public String leave() {
        try {
            ws.leave(authToken, gameId);
            repl.setClient(new LoggedInClient(serverUrl, authToken, repl));
            return "Successfully left game.";
        } catch (IOException e) {
            return "Failed to leave game: " + e.getMessage();
        }
    }


    public String move(String ... params) throws ResponseException {
        try {
            String startingPos = params[1];

            ws.leave(authToken, gameId);
            repl.setClient(new LoggedInClient(serverUrl, authToken, repl));
            return "Successfully left game.";
        } catch (IOException e) {
            return "Failed to leave game: " + e.getMessage();
        }
    }

    public String resign() throws ResponseException {
        try {
            ws.resign(authToken, gameId);
            repl.setClient(new LoggedInClient(serverUrl, authToken, repl));
            return "Successfully resigned.";
        } catch (IOException e) {
            return "Failed to resign: " + e.getMessage();
        }
    }

    public String highlightMoves(String ... params) throws ResponseException{
        return "Game observed";
    }


    public String help() {
        return SET_TEXT_COLOR_MAGENTA + """
                redraw""" + RESET_TEXT_COLOR+ "- the chess board\n"+
                SET_TEXT_COLOR_MAGENTA + "leave" + RESET_TEXT_COLOR+ " - a game\n" +
                SET_TEXT_COLOR_MAGENTA + "move <StartingPos> <EndingPos>" + RESET_TEXT_COLOR + "\n" +
                SET_TEXT_COLOR_MAGENTA + "leave" + RESET_TEXT_COLOR + " - when you are done\n" +
                SET_TEXT_COLOR_MAGENTA + "help" +  RESET_TEXT_COLOR + " - with possible commands";
    }
}