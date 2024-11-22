package ui;

import result.LoginResult;
import result.RegisterResult;

import java.util.Arrays;

import static ui.EscapeSequences.*;

public class PreLoginUI {
    private final ServerFacade facade;

    public PreLoginUI(ServerFacade facade) {
        this.facade = facade;
    }
    public String eval(String input) {
        try {
        var tokens = input.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd) {
            case "register" -> register(params);
            case "login" -> logIn(params);
            case "quit" -> "quit";
            default -> help();
        };
        } catch (Exception e) {
            return "Error, type 'help' for help";
        }

    }
    public String register(String... params) throws Exception {
        if (params.length == 3) {
            RegisterResult result = facade.register(params[0], params[1], params[2]);
            System.out.println("Account created");
            return "a " + result.authToken();
        }
        throw new Exception("Invalid registration, expected <username> <password> <email>");
    }
    public String logIn(String... params) throws Exception {
        if (params.length == 2) {
            LoginResult result = facade.login(params[0], params[1]);
            System.out.println(SET_TEXT_COLOR_BLUE + "Successful sign in\n");
            return "a " + result.authToken();
        }
        throw new Exception("Invalid login, expected <username> <password>");
    }
    public String help() {
        return SET_TEXT_COLOR_YELLOW + """
                Chess Startup help page
                type a command to get started
                """ +
                SET_TEXT_COLOR_LIGHT_GREY + "to create an account - type " +
                SET_TEXT_COLOR_WHITE + "register <username> <password> <email>\n" +
                SET_TEXT_COLOR_LIGHT_GREY + "to log in - type " +
                SET_TEXT_COLOR_WHITE + "login <username> <password>\n" +
                SET_TEXT_COLOR_LIGHT_GREY + "to show help menu - type " +
                SET_TEXT_COLOR_WHITE + "help\n" +
                SET_TEXT_COLOR_LIGHT_GREY + "to quit - type " +
                SET_TEXT_COLOR_WHITE + "quit\n";
    }
}
