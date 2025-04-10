package client;


import client.websocket.NotificationHandler;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.util.Scanner;

import static java.awt.Color.GREEN;
import static java.awt.Color.RED;
import static ui.EscapeSequences.*;

public class Repl implements NotificationHandler {
    private Clients client;
    private String authToken;

    public Repl(String serverUrl) {
        this.client = new UnloggedInClient(serverUrl, this);

    }

    public void setClient(Clients newClient){
        this.client = newClient;
    }

    public void run() {
        System.out.println("\uD83D\uDC36 Welcome to 240 chess. Type Help to get started.");
        System.out.print(client.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                System.out.print(result);

                if (result.contains("Successfully logged in\n") || result.contains("Successfully logged out\n")){
                    System.out.println(client.help());
                }
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    public void notify(ServerMessage message) {
        switch (message.getServerMessageType()) {
            case NOTIFICATION -> {
                NotificationMessage notif = (NotificationMessage) message;
                System.out.println(RED + notif.getNotification());
            }
            case ERROR -> {
                ErrorMessage err = (ErrorMessage) message;
                System.out.println(RED + "ERROR: " + err.getError());
            }
            case LOAD_GAME -> {
                LoadGameMessage gameMsg = (LoadGameMessage) message;
                System.out.println("Game loaded: " + gameMsg.getGame().toString());
            }
        }
        printPrompt();
    }

    private void printPrompt() {
        String status = client.isLoggedIn() ? SET_TEXT_COLOR_BLUE + "[LOGGED_IN]" : SET_TEXT_COLOR_MAGENTA + "[LOGGED_OUT]";
        System.out.print("\n" + RESET_TEXT_COLOR + status + RESET_TEXT_COLOR + " >>> " + SET_TEXT_COLOR_GREEN);
    }

}