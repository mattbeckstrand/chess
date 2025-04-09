package server.websocket;

import com.google.gson.Gson;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.auth.AuthDAO;
import dataaccess.auth.SqlAuthDao;
import dataaccess.gamedata.SqlGameDataDAO;
import model.AuthData;
import exception.ResponseException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.Timer;


@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case CONNECT -> connect(command.getAuthToken(), command.getGameID(), session);
            case LEAVE -> leave(command.getAuthToken(), command.getGameID());
            case RESIGN -> resign(command.getAuthToken(), command.getGameID());
        }
    }

    private String getUsername(String authToken)throws DataAccessException{
        SqlAuthDao authDAO = new SqlAuthDao();
        AuthData auth = authDAO.findAuthByToken(authToken);
        return auth.username();
    }
    private void connect(String authToken, int gameId, Session session) throws IOException {
        connections.add(authToken, gameId, session);
        try {
            String username = getUsername(authToken);
            var message = String.format("%s has joined", username);
            var notification = new NotificationMessage(message);
            connections.broadcastToGame(gameId, authToken, notification);
        } catch (DataAccessException ex) {
            throw new IOException("failed to look up username", ex);
        }
    }

    private void leave(String authToken, int gameId) throws IOException {
        connections.remove(authToken);
        try {
            String username = getUsername(authToken);
            var message = String.format("%s left the game", username);
            var notification = new NotificationMessage(message);
            connections.broadcastToGame(gameId, authToken, notification);
        } catch (DataAccessException ex) {
            throw new IOException("failed to remove user", ex);
        }
    }

    public void resign(String authToken, int gameId) throws IOException {
        try {
            String username = getUsername(authToken);
            SqlGameDataDAO gameDao = new SqlGameDataDAO();
            gameDao.finishGame(gameId);
            var message = String.format("%s has resigned", username);
            var notification = new NotificationMessage(message);
            connections.broadcastToGame(gameId, authToken, notification);
        } catch (DataAccessException ex) {
            throw new IOException("failed to resign", ex);
        }
    }
}