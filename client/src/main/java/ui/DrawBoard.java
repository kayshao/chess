package ui;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import static ui.EscapeSequences.*;

public class DrawBoard {
    private static final int BOARD_SIZE_SQUARES = 8;
    private static final int SQUARE_SIZE_CHARS = 1;

    public DrawBoard() {}

    public void draw() {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        drawHeadersForward(out);
        drawChessBoard(out);
        drawHeadersForward(out);

        out.print(RESET_BG_COLOR);
    }
    private void drawHeadersForward(PrintStream out) {
        String[] headers = {"a", "b", "c", "d", "e", "f", "g", "h"};
        out.print(EMPTY);
        for (String header : headers) {
            drawHeader(out, header);
        }
        out.println();
        setDark(out);
    }

    private static void drawHeader(PrintStream out, String headerText) {
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(SET_TEXT_COLOR_BLUE);
        out.print(" " + headerText + " ");
    }

    private void drawChessBoard(PrintStream out) {
        for (int row = 0; row < BOARD_SIZE_SQUARES; row++) {
            drawRowNumber(out, BOARD_SIZE_SQUARES - row);
            drawRowOfSquares(out, row);
            drawRowNumber(out, BOARD_SIZE_SQUARES - row);
            out.print(RESET_BG_COLOR);
            out.println();
        }
    }

    private static void drawRowNumber(PrintStream out, int rowNumber) {
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(SET_TEXT_COLOR_BLUE);
        out.print(" " + rowNumber + " ");
    }

    private static void drawRowOfSquares(PrintStream out, int row) {
        for (int col = 0; col < BOARD_SIZE_SQUARES; col++) {
            if ((row + col) % 2 == 0) {
                setLight(out);
            } else {
                setDark(out);
            }
            drawSquare(out, row, col);
        }
    }

    private static void drawSquare(PrintStream out, int row, int col) {
        String piece = getPieceSymbol(row, col);
        for (int i = 0; i < SQUARE_SIZE_CHARS; i++) {
            out.print(" " + piece + " ");
        }
    }

    private static String getPieceSymbol(int row, int col) {
        if (row == 0 || row == 7) {
            String[] pieces = {"R", "N", "B", "Q", "K", "B", "N", "R"};
            return (row == 0) ? pieces[col].toLowerCase() : pieces[col];
        } else if (row == 1) {
            return "p";
        } else if (row == 6) {
            return "P";
        }
        return " ";
    }
    private static void setLight(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void setDark(PrintStream out) {
        out.print(SET_BG_COLOR_DARK_GREEN);
        out.print(SET_TEXT_COLOR_WHITE);
    }
}
