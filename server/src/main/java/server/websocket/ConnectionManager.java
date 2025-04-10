package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    private final ConcurrentHashMap<Integer, Set<Connection>> gameConnections = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Integer> tokenToGameMap = new ConcurrentHashMap<>();



    public void add(String authToken, int gameID, Session session) {
        var connection = new Connection(authToken, session);
        gameConnections.putIfAbsent(gameID, new HashSet<>());
        gameConnections.get(gameID).add(connection);
        tokenToGameMap.put(authToken, gameID);
    }

    public void remove(String authToken) {
        Integer gameID = tokenToGameMap.get(authToken);
        if (gameID != null && gameConnections.containsKey(gameID)) {
            gameConnections.get(gameID).removeIf(c -> c.authToken.equals(authToken));
        }
        tokenToGameMap.remove(authToken);
    }

    public void broadcastToGame(int gameID, String excludeAuthToken, ServerMessage message) throws IOException {
        if (!gameConnections.containsKey(gameID)) {return;}

        var removeList = new HashSet<Connection>();
        for (var c : gameConnections.get(gameID)) {
            if (c.session.isOpen()) {
                if (!c.authToken.equals(excludeAuthToken)) {
                    c.send(new Gson().toJson(message));
                }
            } else {
                removeList.add(c);
            }
        }

        for (var c : removeList) {
            gameConnections.get(gameID).remove(c);
            tokenToGameMap.remove(c.authToken);
        }
    }
    public void sendToClient(String authToken, ServerMessage message) throws IOException {
        Integer gameID = tokenToGameMap.get(authToken);
        if (gameID == null) {return;}

        Set<Connection> connectionsInGame = gameConnections.get(gameID);
        if (connectionsInGame == null) {return;}

        for (Connection c : connectionsInGame) {
            if (c.authToken.equals(authToken) && c.session.isOpen()) {
                c.send(new Gson().toJson(message));
                break;
            }
        }
    }

}