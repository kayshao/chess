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
        String[] headerForward = new String[] {"a", "b", "c", "d", "e", "f", "g", "h"};
        String[] headerBackward = new String[] {"h", "g", "f", "e", "d", "c", "b", "a"};
        out.print(ERASE_SCREEN);

        drawHeaders(out, headerForward);
        drawChessBoardForward(out);
        drawHeaders(out, headerForward);
        out.println();
        drawHeaders(out, headerBackward);
        drawChessBoardBackward(out);
        drawHeaders(out, headerBackward);
    }
    private void drawHeaders(PrintStream out, String[] headers) {
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(SET_TEXT_COLOR_BLUE);
        out.print(EMPTY);
        for (String header : headers) {
            out.print(" " + header + " ");
        }
        out.print(EMPTY);
        out.print(RESET_BG_COLOR);
        out.println();
    }

    private void drawChessBoardForward(PrintStream out) {
        for (int row = 0; row < BOARD_SIZE_SQUARES; row++) {
            drawRowNumber(out, BOARD_SIZE_SQUARES - row);
            drawRowOfSquares(out, row);
            drawRowNumber(out, BOARD_SIZE_SQUARES - row);
            out.print(RESET_BG_COLOR);
            out.println();
        }
    }

    private void drawChessBoardBackward(PrintStream out) {
        for (int row = BOARD_SIZE_SQUARES - 1; row >= 0; row--) {
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
        String[] pieces = {"R", "N", "B", "Q", "K", "B", "N", "R"};
        if (row == 0) {
            return SET_TEXT_COLOR_BLACK + pieces[col];
        } else if (row == 7) {
            return SET_TEXT_COLOR_WHITE + pieces[col];
        } else if (row == 1) {
            return SET_TEXT_COLOR_BLACK + "P";
        } else if (row == 6) {
            return SET_TEXT_COLOR_WHITE + "P";
        }
        return " ";
    }
    private static void setLight(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
    }

    private static void setDark(PrintStream out) {
        out.print(SET_BG_COLOR_DARK_GREEN);
    }
}
