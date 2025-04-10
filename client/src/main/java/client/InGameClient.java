package client;

import exception.ResponseException;
import model.*;
import server.ServerFacade;
import ui.DrawingChessBoard;
import java.util.Arrays;
import java.util.List;

import static ui.EscapeSequences.*;

public class InGameClient implements Clients{
    private final ServerFacade server;
    private final String serverUrl;
    private final Repl repl;
    private final String authToken;

    public InGameClient(String serverUrl, String authToken, Repl repl) {
        this.serverUrl = serverUrl;
        this.server = new ServerFacade(serverUrl);
        this.authToken = authToken;
        this.repl = repl;
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
                case "create" -> create(params);
                case "logout" -> logout();
                case "list" -> list();
                case "play" -> playGame(params);
                case "quit" -> "quit";
                case "observe" -> observe(params);
                default -> help();

            };
        } catch (ResponseException ex) {
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

    public String playGame(String... params) throws ResponseException{
        if (params.length != 2) {
            return "Usage: join <ID> [WHITE|BLACK]";
        }
        String stringGameID = params[0];
        int gameId = Integer.parseInt(stringGameID);
        String playerColor = params[1];
        JoinGameRequest request = new JoinGameRequest(playerColor, gameId);
        server.joinGame(authToken, request);
        DrawingChessBoard.drawChessBoard(System.out, playerColor);
        return "Successfully joined game";
    }

    public String observe(String ... params) throws ResponseException{
        if (params.length != 1) {
            return "Usage: observe <ID>";
        }
        String stringGameId = params[0];
        int gameId = Integer.parseInt(stringGameId);

        DrawingChessBoard.drawChessBoard(System.out, "WHITE");
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