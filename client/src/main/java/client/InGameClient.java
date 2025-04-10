package client;

import chess.*;
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
import java.util.Collection;
import java.util.List;

import static ui.EscapeSequences.*;

public class InGameClient implements Clients{
    private final ServerFacade server;
    private final String serverUrl;
    private final Repl repl;
    private final String authToken;
    private final WebSocketFacade ws;
    private final NotificationHandler notificationHandler;
    private final Integer gameId;
    private final ChessGame.TeamColor teamColor;

    public InGameClient(String serverUrl, String authToken, Repl repl, NotificationHandler notificationHandler, int gameId, WebSocketFacade ws, ChessGame.TeamColor teamColor) throws IOException {
        this.serverUrl = serverUrl;
        this.server = new ServerFacade(serverUrl);
        this.authToken = authToken;
        this.repl = repl;
        this.notificationHandler = notificationHandler;
        this.gameId = gameId;
        this.ws = ws;
        this.teamColor = teamColor;
        ws.connect(authToken, gameId);
    }

    @Override
    public boolean isLoggedIn() {
        return true;
    }

    @Override
    public boolean inGame(){
        return true;
    }

    @Override
    public String eval(String input) {
        try {
            var tokens = input.split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "redraw" -> redraw();
                case "leave" -> leave();
                case "move" -> move(params);
                case "resign" -> resign();
                case "highlight" -> highlightMoves(params);
                default -> help();

            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    @Override
    public ChessGame.TeamColor getTeamColor() {
        return teamColor;
    }

    public String redraw() throws ResponseException {
        GameData gameData = server.getGame(authToken, gameId);
        ChessGame game = gameData.getGame();
        DrawingChessBoard.drawChessBoard(System.out, game, teamColor, null);
        return "Successful redraw";
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
            if (params.length != 2) {
                return "Usage: play <STARTINGPOS> <ENDINGPOS>";
            }

            String startingPos = params[0];
            String endingPos = params[1];
            ChessPosition startPosition = parsePosition(startingPos);
            ChessPosition endPosition = parsePosition(endingPos);

            ChessMove move = new ChessMove(startPosition, endPosition, null);
            ws.makeMove(authToken, gameId, move);
            return "Move made successfully";
        } catch (IOException e) {
            return "Failed to send move: " + e.getMessage();
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


    private ChessPosition parsePosition(String pos) {
        if (pos.length() != 2) throw new IllegalArgumentException("Invalid position: " + pos);
        char file = pos.charAt(0);
        char rank = pos.charAt(1);
        int column = file - 'a' + 1;
        int row = Character.getNumericValue(rank);
        return new ChessPosition(row, column);
    }

    public String highlightMoves(String ... params) throws ResponseException {
        if (params.length != 1) {
            return "Usage: highlight <PiecePos>";
        }

        ChessPosition pos = parsePosition(params[0]);
        GameData data = server.getGame(authToken, gameId);
        ChessGame game = data.getGame();
        Collection<ChessMove> validMoves = game.validMoves(pos);

        if (validMoves == null || validMoves.isEmpty()) {
            return "No valid moves for that piece.";
        }

        List<ChessPosition> highlights = validMoves.stream()
                .map(ChessMove::getEndPosition)
                .toList();

        DrawingChessBoard.drawChessBoard(System.out, game, teamColor, highlights);
        return "Moves Highlighted";
    }


    public String help() {
        return SET_TEXT_COLOR_MAGENTA + """
                redraw""" + RESET_TEXT_COLOR+ "- the chess board\n"+
                SET_TEXT_COLOR_MAGENTA + "leave" + RESET_TEXT_COLOR+ " - a game\n" +
                SET_TEXT_COLOR_MAGENTA + "move <StartingPos> <EndingPos>" + RESET_TEXT_COLOR + "\n" +
                SET_TEXT_COLOR_MAGENTA + "highlight" + RESET_TEXT_COLOR + " - moves\n" +
                SET_TEXT_COLOR_MAGENTA + "help" +  RESET_TEXT_COLOR + " - with possible commands";
    }
}