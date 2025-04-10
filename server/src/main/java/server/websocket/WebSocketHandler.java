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
import java.util.Objects;
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
            case RESIGN -> resign(command.getAuthToken(), command.getGameID(), session);
            case MAKE_MOVE -> makeMove(command.getAuthToken(), command.getGameID(), command.getMove(), session);
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

    private void sendErrorToSession(Session session, String message) throws  IOException {
        var error = new ErrorMessage("Error: " + message);
        String json = new Gson().toJson(error);
        session.getRemote().sendString(json);
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
            SqlGameDataDAO gameDao = new SqlGameDataDAO();
            String username = getUsername(authToken);
            GameData gameData = gameDao.getGame(gameId);
            if (username.equals(gameData.getWhiteUsername())){
                gameDao.setUserNull(gameId, ChessGame.TeamColor.WHITE);
            }
            else{
                gameDao.setUserNull(gameId, ChessGame.TeamColor.BLACK);
            }
            var message = String.format("%s left the game", username);
            var notification = new NotificationMessage(message);
            connections.broadcastToGame(gameId, authToken, notification);
        } catch (DataAccessException ex) {
            throw new IOException("failed to remove user", ex);
        }
    }

    public void resign(String authToken, int gameId, Session session) throws IOException {
        try {
            String username = getUsername(authToken);
            SqlGameDataDAO gameDao = new SqlGameDataDAO();
            GameData gameData = gameDao.getGame(gameId);
            SqlAuthDao authDao = new SqlAuthDao();
            AuthData authData = authDao.findAuthByToken(authToken);
            if (authData == null){
                sendErrorToSession(session, "Auth Data null");
                return;
            }
            if (!Objects.equals(gameData.getWhiteUsername(), username) && !Objects.equals(gameData.getBlackUsername(), username)){
                connections.sendToClient(authToken, new ErrorMessage("Error: user not in game cannot resign"));
                return;
            }
            if (gameData.getIsOver()){
                connections.sendToClient(authToken, new ErrorMessage("Error: other user already resigned or game is over"));
                return;
            }
            gameDao.finishGame(gameId);
            var message = String.format("%s has resigned", username);
            var notification = new NotificationMessage(message);
            connections.broadcastToGame(gameId, authToken, notification);
            connections.sendToClient(authToken, notification);
        } catch (DataAccessException ex) {
            throw new IOException("failed to resign", ex);
        }
    }

    private ChessGame.TeamColor getColor(String authToken, int gameId) throws DataAccessException {
        String username = getUsername(authToken);
        SqlGameDataDAO gameDao = new SqlGameDataDAO();
        GameData gameData = gameDao.getGame(gameId);
        if (gameData.getWhiteUsername().equals(username)){
            return ChessGame.TeamColor.WHITE;
        }
        else {
            return ChessGame.TeamColor.BLACK;
        }
    }
    private Boolean checkIsOverFlag(int gameId) throws DataAccessException {
        SqlGameDataDAO gameDao = new SqlGameDataDAO();
        GameData gameData = gameDao.getGame(gameId);
        return gameData.getIsOver();

    }

    public void makeMove(String authToken, int gameId, ChessMove move, Session session) throws IOException{
        try {
            SqlAuthDao authDao = new SqlAuthDao();
            AuthData authData = authDao.findAuthByToken(authToken);

            if (authData == null){
                sendErrorToSession(session, "Auth Data null");
                return;
            }
            if (checkIsOverFlag(gameId)){
                connections.sendToClient(authToken, new ErrorMessage("No moves acceptable, game Resigned"));
                return;
            }
            SqlGameDataDAO gameDao = new SqlGameDataDAO();
            ChessGame game = getGame(gameId);
            if (game.getBoard().getPiece(move.getStartPosition()).getTeamColor() != getColor(authToken, gameId)) {
                throw new InvalidMoveException("Wrong team color");
            }
            game.makeMove(move);

            ChessPosition endPos = move.getEndPosition();
            String serilalizedPos = new Gson().toJson(endPos);
            String serializedGame = new Gson().toJson(game);
            gameDao.updateGameMove(serializedGame, gameId);
            LoadGameMessage loadGameNotif;
            if (game.isInCheckmate(ChessGame.TeamColor.WHITE) || game.isInCheckmate(ChessGame.TeamColor.BLACK)) {
                gameDao.finishGame(gameId);
                ChessGame finishedGame = getGame(gameId);
                loadGameNotif = new LoadGameMessage(finishedGame);
            }
            else {
                loadGameNotif = new LoadGameMessage(game);
            }
            String message = String.format("User has moved to %s", serilalizedPos);
            var notification = new NotificationMessage(message);
            connections.broadcastToGame(gameId, authToken, loadGameNotif);
            connections.sendToClient(authToken, loadGameNotif);
            connections.broadcastToGame(gameId, authToken, notification);
        }  catch (InvalidMoveException e) {
            var error = new ErrorMessage("Error: " + e.getMessage());
            connections.sendToClient(authToken, error);
        } catch (DataAccessException ex) {
            connections.sendToClient(authToken, new ErrorMessage(ex.getMessage()));
            throw new IOException("Move failed", ex);

        }
    }
}