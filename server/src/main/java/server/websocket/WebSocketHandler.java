package server.websocket;

import chess.*;
import com.google.gson.Gson;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.auth.AuthDAO;
import dataaccess.auth.SqlAuthDao;
import dataaccess.gamedata.SqlGameDataDAO;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import javax.xml.crypto.Data;
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
            case MAKE_MOVE -> makeMove(command.getAuthToken(), command.getGameID(), command.getMove());
        }
    }

    private String getUsername(String authToken)throws DataAccessException{
        SqlAuthDao authDAO = new SqlAuthDao();
        AuthData auth = authDAO.findAuthByToken(authToken);
        if (auth == null) {
            throw new DataAccessException("No auth found");
        }
        return auth.username();
    }

    private ChessGame getGame(int gameId) throws DataAccessException {
        SqlGameDataDAO gameDao = new SqlGameDataDAO();
        GameData gameData = gameDao.getGame(gameId);
        if (gameData == null) {
            throw new DataAccessException("No game found with ID: " + gameId);
        }
        return gameData.getGame();
    }
    private void connect(String authToken, int gameId, Session session) throws IOException {
        connections.add(authToken, gameId, session);
        try {
            String username = getUsername(authToken);
            var message = String.format("%s has joined", username);
            ChessGame game = getGame(gameId);
            var notification = new NotificationMessage(message);
            var loadGame = new LoadGameMessage(game);
            connections.broadcastToGame(gameId, authToken, notification);
            connections.sendToClient(authToken, loadGame);
        } catch (DataAccessException ex) {
            var error = new ErrorMessage("Failed to connect: " + ex.getMessage());
            connections.sendToClient(authToken, error);
            connections.remove(authToken);
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
            connections.sendToClient(authToken, notification);
        } catch (DataAccessException ex) {
            throw new IOException("failed to resign", ex);
        }
    }


    public void makeMove(String authToken, int gameId, ChessMove move) throws IOException{
        try {
            SqlGameDataDAO gameDao = new SqlGameDataDAO();
            ChessGame game = getGame(gameId);
            if
            game.makeMove(move);

            ChessPosition endPos = move.getEndPosition();
            String serilalizedPos = new Gson().toJson(endPos);
            String serializedGame = new Gson().toJson(game);
            gameDao.updateGameMove(serializedGame, gameId);
            var loadGameNotif = new LoadGameMessage(game);
            String message = String.format("User has moved to %s", serilalizedPos);
            var notification = new NotificationMessage(message);
            connections.broadcastToGame(gameId, authToken, loadGameNotif);
            connections.sendToClient(authToken, loadGameNotif);
            connections.broadcastToGame(gameId, authToken, notification);
        } catch (DataAccessException ex) {
            throw new IOException("Move failed", ex);
        } catch (InvalidMoveException e) {
            throw new RuntimeException(e);
        }
    }
}