package client.websocket;

import chess.ChessMove;
import com.google.gson.Gson;
import exception.ResponseException;
import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import websocket.messages.ServerMessage;
import websocket.commands.UserGameCommand;


public class WebSocketFacade extends Endpoint {

    Session session;
    NotificationHandler notificationHandler;


    public WebSocketFacade(String url, NotificationHandler notificationHandler) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    Gson gson = new Gson();
                    var jsonObject = gson.fromJson(message, com.google.gson.JsonObject.class);
                    String type = jsonObject.get("serverMessageType").getAsString();
                    ServerMessage notification = switch (type) {
                        case "ERROR" -> gson.fromJson(message, websocket.messages.ErrorMessage.class);
                        case "NOTIFICATION" -> gson.fromJson(message, websocket.messages.NotificationMessage.class);
                        case "LOAD_GAME" -> gson.fromJson(message, websocket.messages.LoadGameMessage.class);
                        case null, default -> gson.fromJson(message, ServerMessage.class);
                    };
                    notificationHandler.notify(notification);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void send(UserGameCommand command) throws IOException {
        String json = new Gson().toJson(command);
        session.getBasicRemote().sendText(json);
    }

    public void connect(String authToken, int gameID) throws IOException {
        UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID, null);
        send(command);
    }
    public void makeMove(String authToken, int gameId, ChessMove move) throws IOException {
        UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.MAKE_MOVE, authToken, gameId, move);
        send(command);
    }

    public void leave(String authToken, int gameId) throws IOException {
        UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameId, null);
        send(command);
    }
    public void resign(String authToken, int gameId) throws IOException {
        UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameId, null);
        send(command);
    }

}