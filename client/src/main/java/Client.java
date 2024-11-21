import ui.PostLoginUI;
import ui.PreLoginUI;
import ui.ServerFacade;

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
        System.out.println("Welcome to Chess!");
        runPreLoginUI();
        runPostLoginUI();
    }
    private void runPreLoginUI() {
        PreLoginUI preLoginUI = new PreLoginUI(server);
        var result = "";
        while (!result.equals("quit")) {
            System.out.print("\n" + SET_TEXT_COLOR_MAGENTA + "Chess Home>>> ");
            String line = scanner.nextLine();

            try {
                result = preLoginUI.eval(line);
                System.out.print(SET_TEXT_COLOR_BLUE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    };

    private void runPostLoginUI() {
        PostLoginUI postLoginUI = new PostLoginUI(server);
        var result = "";
        while (!result.equals("quit")) {
            System.out.print(SET_TEXT_COLOR_MAGENTA + "Logged in to Chess>>> ");
            String line = scanner.nextLine();

            try {
                // result = postLoginUI.eval(line);
                System.out.print(SET_TEXT_COLOR_BLUE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    };
}
