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
import java.io.IOException;
import java.util.Objects;

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
            var notification1 = new NotificationMessage("You have resigned");
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

    private boolean isPlayer(int gameId, String authToken) throws DataAccessException {
        SqlGameDataDAO gameDao = new SqlGameDataDAO();
        GameData gameData = gameDao.getGame(gameId);
        String username = getUsername(authToken);
        if (gameData.getWhiteUsername() != username && gameData.getBlackUsername() != username) {
            return false;
        }
        return true;
    }
    public void makeMove(String authToken, int gameId, ChessMove move, Session session) throws IOException {
        try {
            SqlAuthDao authDao = new SqlAuthDao();
            AuthData authData = authDao.findAuthByToken(authToken);
            if (authData == null) {
                sendErrorToSession(session, "bad authToken");
                return;
            }
            if (isPlayer(gameId, authToken)) {
                connections.sendToClient(authToken, new ErrorMessage("Error: not a player"));
                return;
            }
            if (checkIsOverFlag(gameId)) {
                connections.sendToClient(authToken, new ErrorMessage("Error: game is already over, no moves allowed."));
                return;
            }
            ChessGame game = getGame(gameId);
            ChessGame.TeamColor playerColor = getColor(authToken, gameId);
            if (game.getTeamTurn() != playerColor){
                connections.sendToClient(authToken, new ErrorMessage("Error: not your turn!"));
                return;
            }
            ChessPiece startPiece = game.getBoard().getPiece(move.getStartPosition());
            if (startPiece == null) {
                connections.sendToClient(authToken, new ErrorMessage("Error: no piece at start position."));
                return;
            }
            if (startPiece.getTeamColor() != playerColor) {
                connections.sendToClient(authToken, new ErrorMessage("Error: that piece isn't yours."));
                return;
            }

            game.makeMove(move);
            SqlGameDataDAO gameDao = new SqlGameDataDAO();
            gameDao.updateGameMove(new Gson().toJson(game), gameId);

            if (game.isInCheckmate(ChessGame.TeamColor.WHITE) || game.isInCheckmate(ChessGame.TeamColor.BLACK)) {
                gameDao.finishGame(gameId);
                ChessGame finished = getGame(gameId);
                connections.broadcastToGame(gameId, authToken, new LoadGameMessage(finished));
                connections.sendToClient(authToken, new LoadGameMessage(finished));
                connections.broadcastToGame(gameId, authToken, new NotificationMessage("Game over by checkmate!"));
            } else {
                LoadGameMessage updatedGame = new LoadGameMessage(game);
                connections.broadcastToGame(gameId, authToken, updatedGame);
                connections.sendToClient(authToken, updatedGame);

                ChessPosition endPos = move.getEndPosition();
                connections.broadcastToGame(gameId, authToken, new NotificationMessage("Player moved piece from " +
                        toChessNotation(move.getStartPosition())+ " to " + toChessNotation(endPos)));
            }
        } catch (InvalidMoveException e) {
            connections.sendToClient(authToken, new ErrorMessage("Invalid Move: " + e.getMessage()));
        } catch (DataAccessException ex) {
            connections.sendToClient(authToken, new ErrorMessage("Database Error: " + ex.getMessage()));
            throw new IOException("Move failed", ex);
        }
    }
    private String toChessNotation(ChessPosition position) {
        char col = (char) ('a' + position.getColumn() - 1);
        int row = position.getRow();
        return "" + col + row;
    }

}