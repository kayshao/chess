package ui;

import result.ListGamesResult;

import java.util.ArrayList;
import java.util.Arrays;
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
                case "observe" -> observeGame(params);
                default -> help();
            };
        } catch (Exception e) {
            return SET_TEXT_COLOR_RED + "Error: type 'help' for help\n";
        }
    }
    public String logout() {
        try {
            facade.logout(authToken);
            return "Signing out...\n";
        } catch (Exception e) {
            return SET_TEXT_COLOR_RED + "Error: unsuccessful logout\n";
        }
    }
    public String createGame(String... params) throws Exception {
        if (params.length == 1) {
            facade.createGame(params[0], authToken);
            return String.format("Created game %s\n", params[0]);
        } throw new Exception(SET_TEXT_COLOR_RED + "Could not create game\n");
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
            throw new Exception(SET_TEXT_COLOR_RED + "Error listing games\n");
        }
    }
    public String playGame(String... params) throws Exception {
        if (params.length == 2) {
            try {
                int id = Integer.parseInt(params[0].split("\\.")[0]);
                if (id > games.size() || id < 0) {
                    return SET_TEXT_COLOR_RED + "Error: invalid game number\n";
                }
                String color = params[1].toUpperCase();
                if (!(color.equals("BLACK") || color.equals("WHITE"))) {
                    return SET_TEXT_COLOR_RED + "Error: color must be 'black' or 'white'\n";
                }
                facade.joinGame(games.get(id - 1), color, authToken);
                return color + " " + id + " \n";
            } catch (Exception e) {
                return(SET_TEXT_COLOR_RED + "Error: expected <game number> <color>\n");
            }
        }
        throw new Exception(SET_TEXT_COLOR_RED + "Unsuccessful join\n");
    }
    public String observeGame(String... params) throws Exception {
        if (params.length == 1) {
            int id = Integer.parseInt(params[0].split("\\.")[0]);
            if (id > games.size() || id < 0) {
                return SET_TEXT_COLOR_RED + "Error: invalid game number\n";
            }
            return "Observing game\n";
        }
        throw new Exception(SET_TEXT_COLOR_RED + "Error observing game\n");
    }
    public String help() {
        return SET_TEXT_COLOR_YELLOW + """
                Chess user help page
                type a command to get started
                """ +
                SET_TEXT_COLOR_LIGHT_GREY + "to create a game - type" +
                SET_TEXT_COLOR_WHITE + " new <game_name>\n" +
                SET_TEXT_COLOR_LIGHT_GREY + "to see a list of games - type" +
                SET_TEXT_COLOR_WHITE + " list\n" +
                SET_TEXT_COLOR_LIGHT_GREY + "to play a game - type" +
                SET_TEXT_COLOR_WHITE + " play <game_number> <color>\n" +
                SET_TEXT_COLOR_LIGHT_GREY + "to observe a game - type" +
                SET_TEXT_COLOR_WHITE + " observe <game_number>\n" +
                SET_TEXT_COLOR_LIGHT_GREY + "to show help menu - type " +
                SET_TEXT_COLOR_WHITE + "help\n" +
                SET_TEXT_COLOR_LIGHT_GREY + "to log out - type " +
                SET_TEXT_COLOR_WHITE + " logout\n";
    }
}
