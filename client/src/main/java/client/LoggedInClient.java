package client;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import model.*;
import server.ServerFacade;
import ui.DrawingChessBoard;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static ui.EscapeSequences.*;

public class LoggedInClient implements Clients{
    private final ServerFacade server;
    private final String serverUrl;
    private final Repl repl;
    private final String authToken;
    private List<GameSummary> lastListedGames;

    public LoggedInClient(String serverUrl, String authToken, Repl repl) {
        this.serverUrl = serverUrl;
        this.server = new ServerFacade(serverUrl);
        this.authToken = authToken;
        this.repl = repl;
    }

    @Override
    public boolean isLoggedIn() {
        return true;
    }
    @Override
    public boolean inGame(){
        return false;
    }

    public String eval(String input) {
        try {
            var tokens = input.split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "create" -> create(params);
                case "logout" -> logout();
                case "list" -> list();
                case "play" -> playGame(params);
                case "quit" -> "quit";
                case "observe" -> observe(params);
                default -> help();

            };
        } catch (ResponseException | IOException ex) {
            return ex.getMessage();
        }
    }

    public String create(String... params) throws ResponseException {
        if (params.length != 1) {
            return "Usage: create <NAME>";
        }
        String gameName = params[0];
        CreateGameRequest request = new CreateGameRequest(gameName);
        Integer gameID = server.createGame(request, authToken);
        return "Successfully created game";
    }

    public String logout() throws ResponseException{
        server.logout(this.authToken);
        repl.setClient(new UnloggedInClient(serverUrl, repl));
        return "Successfully logged out ";
    }

    public String list() throws ResponseException {
        ListGamesResponse response = server.listGames(authToken);
        StringBuilder result = new StringBuilder();
        List<GameSummary> games = response.games();
        this.lastListedGames = response.games();
        int count = 1;
        for (GameSummary game : games) {
            result.append(count++).append(". ");
            result.append("Game Name: ").append(game.gameName()).append("\n");
            result.append("Players:\n");
            if (game.whiteUsername() != null) {
                result.append("  White: ").append(game.whiteUsername()).append("\n");
            }
            if (game.blackUsername() != null) {
                result.append("  Black: ").append(game.blackUsername()).append("\n");
            }
            result.append("\n");
        }

        return result.toString();
    }

    public String playGame(String... params) throws ResponseException, IOException {
        if (params.length != 2) {
            return "Usage: play <Id> [WHITE|BLACK]";
        }
        int index = Integer.parseInt(params[0]) - 1;
        if (lastListedGames == null || index < 0 || index >= lastListedGames.size()) {
            return "Error: invalid game index. Please use the 'list' command first.";
        }

        int gameId = lastListedGames.get(index).gameID();
        String playerColor = params[1];
        ChessGame.TeamColor tColor = playerColor.equalsIgnoreCase("WHITE") ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;

        JoinGameRequest request = new JoinGameRequest(playerColor, gameId);
        server.joinGame(authToken, request);

        GameData gameData = server.getGame(authToken, gameId);
        ChessGame game = gameData.getGame();
        DrawingChessBoard.drawChessBoard(System.out, game, tColor, null);
        repl.setClientToGame(serverUrl, authToken, gameId, tColor);

        return "Successfully joined game";
    }

    public String observe(String ... params) throws ResponseException, IOException {
        if (params.length != 1) {
            return "Usage: observe <ID>";
        }
        String stringGameId = params[0];
        int gameId = Integer.parseInt(stringGameId);
        repl.setClientToGame(serverUrl, authToken, gameId, ChessGame.TeamColor.WHITE);
        GameData gameData = server.getGame(authToken, gameId);
        ChessGame game = gameData.getGame();
        DrawingChessBoard.drawChessBoard(System.out, game, ChessGame.TeamColor.WHITE, null);
        return "Game observed";
    }


    public String help() {
        return SET_TEXT_COLOR_MAGENTA + """
                create <NAME>""" + RESET_TEXT_COLOR + " - a game\n" +
                SET_TEXT_COLOR_MAGENTA + "list" + RESET_TEXT_COLOR+ " - games\n"+
                SET_TEXT_COLOR_MAGENTA + "play <ID> [WHITE|BLACK]" + RESET_TEXT_COLOR+ " - a game\n" +
                SET_TEXT_COLOR_MAGENTA + "observe <ID>" + RESET_TEXT_COLOR + " - a game\n" +
                SET_TEXT_COLOR_MAGENTA + "logout" + RESET_TEXT_COLOR + " - when you are done\n" +
                SET_TEXT_COLOR_MAGENTA + "help" +  RESET_TEXT_COLOR + " - with possible commands";
    }
}