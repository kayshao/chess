package ui;
import chess.ChessGame;
import static ui.EscapeSequences.*;
import static ui.EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY;

import java.net.http.WebSocket;
import java.util.Arrays;


public class GameplayUI {
    private ChessGame game;
    private WebSocket webSocket;


    public GameplayUI(ChessGame game, WebSocket webSocket) {
        this.game = game;
        this.webSocket = webSocket;
    }
    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "redraw" -> drawBoard();
                case "highlight" -> highlightMoves();
                case "move" -> makeMove(params);
                case "resign" -> resign();
                case "leave" -> leave();
                default -> help();
            };
        } catch (Exception e) {
            return SET_TEXT_COLOR_RED + "Error: type 'help' for help\n";
        }
    }
    public String drawBoard() {
        DrawBoard b = new DrawBoard();
        b.draw();
        return null;
    }
    public String highlightMoves() {
        return null;
    }

    public String makeMove(String... params) {
        return null;
    }

    public String resign() {
        return null;
    }

    public String leave() {
        return null;
    }

    public String help() {
        return SET_TEXT_COLOR_YELLOW + """
                Chess gameplay help page
                type a command to get started
                """ +
                SET_TEXT_COLOR_LIGHT_GREY + "to redraw the board - type" +
                SET_TEXT_COLOR_WHITE + " redraw\n" +
                SET_TEXT_COLOR_LIGHT_GREY + "to highlight legal moves - type" +
                SET_TEXT_COLOR_WHITE + " highlight\n" +
                SET_TEXT_COLOR_LIGHT_GREY + "to make a move - type" +
                SET_TEXT_COLOR_WHITE + " move <move>\n" +
                SET_TEXT_COLOR_LIGHT_GREY + "to resign - type" +
                SET_TEXT_COLOR_WHITE + " resign\n" +
                SET_TEXT_COLOR_LIGHT_GREY + "to leave game - type" +
                SET_TEXT_COLOR_WHITE + " leave\n" +
                SET_TEXT_COLOR_LIGHT_GREY + "for help - type" +
                SET_TEXT_COLOR_WHITE + " help\n";
    }


}
