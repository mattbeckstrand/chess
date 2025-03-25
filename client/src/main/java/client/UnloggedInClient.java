package client;

import exception.ResponseException;
import model.AuthData;
import model.LoginRequest;
import model.UserData;
import server.ServerFacade;

import java.util.Arrays;

import static ui.EscapeSequences.*;

public class UnloggedInClient implements Clients{
    private final ServerFacade server;
    private final String serverUrl;
    private final Repl repl;

    public UnloggedInClient(String serverUrl, Repl repl) {
        this.serverUrl = serverUrl;
        this.server = new ServerFacade(serverUrl);
        this.repl = repl;
    }

    @Override
    public boolean isLoggedIn() {
        return false;
    }

    @Override
    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> login(params);
                case "quit" -> "quit";
                default -> help();

            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String register(String... params) throws ResponseException {
        if (params.length != 3) {
            return "Usage: register <username> <password> <email>";
        }
        String username = params[0];
        String password = params[1];
        String email = params[2];
        UserData userData = new UserData(username, password, email);
        try {
            AuthData authData = server.register(userData);
            String authToken = authData.authToken();
            repl.setClient(new LoggedInClient(serverUrl, authToken, repl));
            return "Successfully registered user: " + username;
        } catch (ResponseException e) {
            return "Registration failed: " + e.getMessage();
        }
    }

    public String login(String... params) throws ResponseException{
        if (params.length != 2) {
            return "Usage: login <username> <password>";
        }
        String username = params[0];
        String password = params[1];
        LoginRequest loginRequest = new LoginRequest(username, password);
        String authToken = server.login(loginRequest);
        repl.setClient(new LoggedInClient(serverUrl, authToken, repl));
        return "Successfully logged in user: " + username;
    }
    @Override
    public String help() {
        return SET_TEXT_COLOR_BLUE + """
                register <USERNAME> <PASSWORD> <EMAIL>""" +  RESET_TEXT_COLOR + " - to create an account \n" +
                SET_TEXT_COLOR_BLUE + "login <USERNAME> <PASSWORD>" + RESET_TEXT_COLOR + " - to play chess \n" +
                SET_TEXT_COLOR_BLUE + "quit" + RESET_TEXT_COLOR + " - playing chess\n"+
                SET_TEXT_COLOR_BLUE + "help" + RESET_TEXT_COLOR + " - with possible commands";
    }

}