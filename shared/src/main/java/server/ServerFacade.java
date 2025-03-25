package server;

import com.google.gson.Gson;
import exception.ResponseException;
import model.*;


import java.io.*;
import java.net.*;

public class ServerFacade {
    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public AuthData register(UserData userData) throws ResponseException{
        var path = "/user";
        return this.makeRequest("POST", path, userData, AuthData.class);
    }

    public String login(LoginRequest loginRequest) throws ResponseException{
        var path = "/session";
        var response = this.makeRequest("POST", path, loginRequest, AuthData.class);
        return response.authToken();
    }

    public void logout(String authToken) throws ResponseException{
        var path = "/session";
        this.makeRequestWithAuth("DELETE", path, authToken, null, null);
    }

    public Integer createGame(CreateGameRequest request, String authToken) throws ResponseException{
        var path = "/game";
        CreateGameResponse response =  this.makeRequestWithAuth("POST", path, authToken, request, CreateGameResponse.class);
        return response.gameID();
    }


    public void joinGame(String authToken, JoinGameRequest request) throws ResponseException{
        var path = "/game";
        this.makeRequestWithAuth("PUT", path, authToken, request, null);
    }

    public ListGamesResponse listGames(String authToken) throws ResponseException{
        var path = "/game";
        return this.makeRequestWithAuth("GET", path, authToken, null, ListGamesResponse.class);
    }

    public void delete() throws ResponseException{
        var path = "/db";
        this.makeRequest("DELETE", path, null, null);
    }


    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);
            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (ResponseException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private <T> T makeRequestWithAuth(String method, String path, String authToken, Object request, Class<T> responseClass) throws ResponseException{
        try{
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setRequestProperty("Authorization", authToken);

            if (request != null) {
                http.setDoOutput(true);
                writeBody(request, http);
            }
            http.connect();
            throwIfNotSuccessful(http);

            if (responseClass != null) {
                return readBody(http, responseClass);
            } else {
                return null;
            }
        } catch (ResponseException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            try (InputStream respErr = http.getErrorStream()) {
                if (respErr != null) {
                    throw ResponseException.fromJson(respErr);
                }
            }

            throw new ResponseException(status, "other failure: " + status);
        }
    }
    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        if (responseClass == null){
            return null;
        }
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                T response = new Gson().fromJson(reader, responseClass);
                return response;
            }
        }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}