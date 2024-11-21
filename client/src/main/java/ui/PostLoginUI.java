package ui;
import result.*;

import java.util.Arrays;

import static ui.EscapeSequences.*;

public class PostLoginUI {
    private final ServerFacade facade;

    public PostLoginUI(ServerFacade facade) {
        this.facade = facade;
    }
    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                // case "logout" -> logout(params);
                // case "new" -> createGame(params);
                // case "list" -> listGames(params);
                // case "play" -> playGame(params);
                // case "observe" -> observeGame(params);
                default -> help();
            };
        } catch (Exception e) {
            return e.getMessage();
        }
    }
    public String help() {
        return SET_TEXT_COLOR_YELLOW + """
                Chess user help page
                type a command to get started
                """ +
                SET_TEXT_COLOR_WHITE + "to create a game" +
                SET_TEXT_COLOR_LIGHT_GREY + " - new <game_name>\n" +
                SET_TEXT_COLOR_WHITE + "to see a list of games" +
                SET_TEXT_COLOR_LIGHT_GREY + " - list\n" +
                SET_TEXT_COLOR_WHITE + "to play a game" +
                SET_TEXT_COLOR_LIGHT_GREY + " - play <game_number> <color>\n" +
                SET_TEXT_COLOR_WHITE + "to observe a game" +
                SET_TEXT_COLOR_LIGHT_GREY + " - observe <game_number>\n" +
                SET_TEXT_COLOR_WHITE + "to log out" +
                SET_TEXT_COLOR_LIGHT_GREY + " - logout";
    }
}
