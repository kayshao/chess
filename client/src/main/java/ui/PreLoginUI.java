package ui;
import result.*;
import ui.EscapeSequences;

import java.util.Arrays;

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
            // case "quit" -> quit();
            default -> register();
        };
        } catch (Exception e) {
            System.out.println("a slight error");
            return e.getMessage();
        }

    }
    public String register(String... params) throws Exception {
        if (params.length >= 3) {
            RegisterResult result = facade.register(params[0], params[1], params[2]);
            return String.format("Account created, username: %s", result.username());
        }
        throw new Exception("Invalid registration, expected <username> <password> <email>");
    }
    public String logIn(String... params) throws Exception {
        if (params.length >= 2) {
            facade.login(params[0], params[1]);
            return "Successful sign in";
        }
        throw new Exception("Invalid login");
    }
}
