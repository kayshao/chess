package ui;
import result.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static ui.EscapeSequences.*;

public class PostLoginUI {
    private final ServerFacade facade;
    private final String authToken;
    private ArrayList<Integer> games;

    public PostLoginUI(ServerFacade facade, String auth) {
        this.facade = facade;
        this.authToken = auth;
    }
    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "logout" -> logout();
                case "new" -> createGame(params);
                case "list" -> listGames();
                case "play" -> playGame(params);
                // case "observe" -> observeGame(params);
                default -> help();
            };
        } catch (Exception e) {
            return e.getMessage();
        }
    }
    public String logout() throws Exception {
        try {
            facade.logout(authToken);
            return "Signing out...\n";
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
    public String createGame(String... params) throws Exception {
        if (params.length >= 1) {
            facade.createGame(params[0], authToken);
            return String.format("Created game %s\n", params[0]);
        } throw new Exception("Could not create game\n");
    }
    public String listGames() throws Exception {
        try {
            ListGamesResult result = facade.listGames(authToken);
            ArrayList<Integer> games = new ArrayList<>();
            int gameNumber = 1;
            for (Map<String, String> game : result.games()) {
                int gameID = Integer.parseInt(game.get("gameID"));
                games.add(gameID);
                String gameName = game.get("gameName");
                String whitePlayer = game.get("whiteUsername");
                String blackPlayer = game.get("blackUsername");
                System.out.printf(SET_TEXT_COLOR_BLUE + "%d. %s - White: %s, Black: %s%n",
                        gameNumber++, gameName,
                        whitePlayer != null ? whitePlayer : "EMPTY",
                        blackPlayer != null ? blackPlayer : "EMPTY");
            }
            this.games = games;
            return "";
        } catch (Exception e) {
            throw new Exception("Error listing games: " + e.getMessage());
        }
    }
    public String playGame(String... params) throws Exception {
        if (params.length >= 2) {
            try {
                int id = Integer.parseInt(params[0].split("\\.")[0]);
                facade.joinGame(games.get(id) - 1, params[1].toUpperCase(), authToken);
                return "Join game successful\n";
            } catch (Exception e) {
                return(e+"\n");
            }
        }
        throw new Exception("Unsuccessful join\n");
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
                SET_TEXT_COLOR_LIGHT_GREY + " - logout\n";
    }
}
