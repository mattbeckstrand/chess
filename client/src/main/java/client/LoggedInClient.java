package client;

import exception.ResponseException;
import model.AuthData;
import model.CreateGameRequest;
import model.LoginRequest;
import model.UserData;
import server.ServerFacade;

import java.util.Arrays;
import exception.ResponseException;
import server.ServerFacade;

import java.util.Arrays;

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
                case "login" -> login(params);
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

    public String login(String... params) throws ResponseException{
        if (params.length != 3) {
            return "Usage: register <username> <password> <email>";
        }
        String username = params[0];
        String password = params[1];
        LoginRequest loginRequest = new LoginRequest(username, password);
        String authToken = server.Login(loginRequest);
        this.state = State.SIGNEDIN;
        return "Successfully logged in user: " + username;
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