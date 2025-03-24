package client;

import exception.ResponseException;
import model.*;
import server.ServerFacade;

import java.util.Arrays;
import exception.ResponseException;
import server.ServerFacade;

import java.util.Arrays;
import java.util.List;

public class LoggedInClient implements Clients{
    private final ServerFacade server;
    private State state = State.SIGNEDOUT;
    private final String serverUrl;
    private final Repl repl;
    private final String authToken;

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

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "create" -> create(params);
                case "logout" -> logout();
                case "list" -> list();
                case "play" -> playGame(params);
                case "quit" -> "quit";
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
        Integer gameID = server.CreateGame(request, authToken);
        return "Successfully created game: " + gameID;
    }

    public String logout() throws ResponseException{
        server.Logout(this.authToken);
        repl.setClient(new UnloggedInClient(serverUrl, repl));
        return "Successfully logged out ";
    }

    public String list() throws ResponseException {
        ListGamesResponse response = server.ListGames(authToken);
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
        server.JoinGame(authToken, request);
        return "Successfully joined game";
    }


    public String help() {
        return """
                create <NAME> - a game
                list - games
                join <ID> [WHITE|BLACK] - a game
                observe <ID> - a game
                logout - when you are done
                help - with possible commands
                """;
    }

}