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
                case "move" -> makeMove();
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

    public String makeMove() {
        return null;
    }

    public String resign() {
        return null;
    }

    public String leave() {
        return null;
    }

    public String help() {
     return null;
    }


}
