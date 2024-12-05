import chess.ChessGame;
import ui.GameplayUI;
import ui.PostLoginUI;
import ui.PreLoginUI;
import ui.ServerFacade;

import java.net.http.WebSocket;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Client {
    private final ServerFacade server;
    private final Scanner scanner;

    public Client(String url) {
        server = new ServerFacade(url);
        scanner = new Scanner(System.in);
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
                System.out.print(SET_TEXT_COLOR_BLUE + result);
                if (result.startsWith("Transitioning")) {
                    runGameplayUI();
                }
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }
    private void runGameplayUI() {
        GameplayUI gameplayUI = new GameplayUI(new ChessGame());
        var result = "";
        while (true) {
            System.out.print(SET_TEXT_COLOR_MAGENTA + "In Chess Gameplay>>> ");
            String line = scanner.nextLine();
            try {
                result = gameplayUI.eval(line);
                System.out.print(SET_TEXT_COLOR_BLUE + result);

            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        // System.out.println();
    }
}
