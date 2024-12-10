package ui;
import chess.ChessBoard;
import chess.ChessGame;
import websocket.NotificationHandler;
import websocket.WebSocketFacade;

import static ui.EscapeSequences.*;
import java.util.Arrays;


public class GameplayUI implements NotificationHandler {
    private ChessGame game;
    private WebSocketFacade webSocket;
    private String color;
    private String url;
    private String auth;
    private Integer id;


    public GameplayUI(String url, ChessGame game, String color, String authToken, Integer gameID) {
        this.game = game;
        this.color = color;
        this.url = url;
        this.auth = authToken;
        this.id = gameID;
        this.setUp();
    }
    public void setUp() {
        System.out.println("SETTING UP");   // TODO: remove after debugging
        try {
            this.webSocket = new WebSocketFacade(url, this);
            System.out.println("websocket created"); // TODO: remove after dbuggig
            webSocket.connect(this.auth, this.id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "redraw" -> drawBoard(game.board, color);
                case "highlight" -> highlightMoves();
                case "move" -> makeMove(params);
                case "resign" -> resign();
                case "leave" -> leave();
                default -> help();
            };
        } catch (Exception e) {
            return SET_TEXT_COLOR_RED + e.getMessage();//ToDO: "Error: type 'help' for help\n";
        }
    }
    public String drawBoard(ChessBoard board, String color) {
        DrawBoard b = new DrawBoard(board);
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

    public String leave() throws Exception {
        webSocket.disconnect(auth, id);
        return "Leaving game";
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
    public void updateGame(ChessGame game) {
        System.out.println("Updating game\n");
    }
    public void showNotification(String msg) {}
    public void showError(String msg) {}

}
