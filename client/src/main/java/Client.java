import chess.ChessGame;
import ui.GameplayUI;
import ui.PostLoginUI;
import ui.PreLoginUI;
import ui.ServerFacade;
import websocket.NotificationHandler;

import java.net.http.WebSocket;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Client {
    private final ServerFacade server;
    private final Scanner scanner;
    private final String url;

    public Client(String url) {
        server = new ServerFacade(url);
        scanner = new Scanner(System.in);
        this.url = url;
    }

    public void run() {
        System.out.println("Welcome to Chess! Type 'help' for help.");
        runPreLoginUI();
    }
    private void runPreLoginUI() {
        PreLoginUI preLoginUI = new PreLoginUI(server);
        var result = "";
        while (!result.equals("quit")) {
            System.out.print("\n" + SET_TEXT_COLOR_MAGENTA + "Chess Startup>>> ");
            String line = scanner.nextLine();

            try {
                result = preLoginUI.eval(line);
                if (result.startsWith("a")) {
                    runPostLoginUI(result.split(" ")[1]);
                    result = "Welcome to Chess Startup";
                }
                System.out.print(SET_TEXT_COLOR_BLUE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println("ting Chess... goodbye");
    };

    private void runPostLoginUI(String auth) {
        PostLoginUI postLoginUI = new PostLoginUI(server, auth);
        var result = "";
        while (!result.equals("Signing out...\n")) {
            System.out.print(SET_TEXT_COLOR_MAGENTA + "Logged in to Chess>>> ");
            String line = scanner.nextLine();
            try {
                result = postLoginUI.eval(line);
                if (result.startsWith("WHITE")) {
                    runGameplayUI("WHITE", auth, Integer.valueOf(result.split(" ")[1]));
                    System.out.print(SET_TEXT_COLOR_BLUE + "Joining game as white");
                }
                else if (result.startsWith("BLACK")) {
                    runGameplayUI("BLACK", auth, Integer.valueOf(result.split(" ")[1]));
                    System.out.print(SET_TEXT_COLOR_BLUE + "Joining game as black");
                }
                else if (result.startsWith("Observing")) {
                    runGameplayUI(null, auth, Integer.valueOf(result.split(" ")[1]));
                    System.out.print(SET_TEXT_COLOR_BLUE + "Observing game");
                }
                else {
                    System.out.print(SET_TEXT_COLOR_BLUE + result);
                }
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }
    private void runGameplayUI(String color, String auth, Integer gameID) {   // string will be color if playing and null if observing
        GameplayUI gameplayUI = new GameplayUI(this.url, new ChessGame(), color, auth, gameID);
        var result = "";
        while (!result.equals("Leaving game")) {
            System.out.print(SET_TEXT_COLOR_MAGENTA + "\nIn Chess Gameplay>>> ");
            String line = scanner.nextLine();
            try {
                result = gameplayUI.eval(line);
                // TODO: sometimes prints "null" System.out.print(SET_TEXT_COLOR_BLUE + result);

            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }
}
