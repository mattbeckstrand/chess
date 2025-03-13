import chess.*;
import dataaccess.DataAccessException;
import exception.ResponseException;
import server.Server;
import dataaccess.DatabaseManager;

import javax.xml.crypto.Data;

public class Main {
    public static void main(String[] args) throws DataAccessException {
        Server server = new Server();
        int port = server.run(8000);
        System.out.println("Chess server started on port: " + port);
    }
}