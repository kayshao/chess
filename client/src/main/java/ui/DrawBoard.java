package ui;
import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static ui.EscapeSequences.*;

public class DrawBoard {
    private static final int BOARD_SIZE_SQUARES = 8;
    private static final int SQUARE_SIZE_CHARS = 1;
    private final ChessBoard board;
    private static final Map<ChessPiece.PieceType, String> pieceMap = Map.of(
            ChessPiece.PieceType.KING, "K",
            ChessPiece.PieceType.QUEEN, "Q",
            ChessPiece.PieceType.ROOK, "R",
            ChessPiece.PieceType.BISHOP, "B",
            ChessPiece.PieceType.KNIGHT, "N",
            ChessPiece.PieceType.PAWN, "P"
    );


    public DrawBoard(ChessBoard board) {
        this.board = board;
    }

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
            drawRowOfSquares(out, board, row);
            drawRowNumber(out, BOARD_SIZE_SQUARES - row);
            out.print(RESET_BG_COLOR);
            out.println();
        }
    }

    private void drawChessBoardBackward(PrintStream out) {
        for (int row = BOARD_SIZE_SQUARES - 1; row >= 0; row--) {
            drawRowNumber(out, BOARD_SIZE_SQUARES - row);
            drawRowOfSquaresBackward(out, board, row);
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

    private static void drawRowOfSquares(PrintStream out, ChessBoard board, int row) {
        for (int col = 0; col < BOARD_SIZE_SQUARES; col++) {
            if ((row + col) % 2 == 0) {
                setLight(out);
            } else {
                setDark(out);
            }
            drawSquare(out, board, row, col, false);
        }
    }

    private static void drawRowOfSquaresBackward(PrintStream out, ChessBoard board, int row) {
        for (int col = BOARD_SIZE_SQUARES - 1; col >= 0; col--) {
            if ((row + col) % 2 != 0) {
                setLight(out);
            } else {
                setDark(out);
            }
            drawSquare(out, board, row, col, true);
        }
    }

    private static void drawSquare(PrintStream out, ChessBoard board, int row, int col, boolean back) {
        if (back) {
            String piece = getPieceSymbol(board, row, col);
            for (int i = 0; i < SQUARE_SIZE_CHARS; i++) {
                out.print(" " + piece + " ");
            }
        } else {
            String piece = getPieceSymbol(board, row, col);
            for (int i = 0; i < SQUARE_SIZE_CHARS; i++) {
                out.print(" " + piece + " ");
            }
        }
    }

    private static String getPieceSymbol(ChessBoard board, int row, int col) {
        ChessPiece piece = board.getPiece(new ChessPosition(row+1, col+1));
        if (piece == null) {
            return " ";
        }
        else if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            return SET_TEXT_COLOR_WHITE + pieceMap.get(piece.getPieceType());
        }
        else {
            return SET_TEXT_COLOR_BLACK + pieceMap.get(piece.getPieceType());
        }
    }

    private static void setLight(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
    }

    private static void setDark(PrintStream out) {
        out.print(SET_BG_COLOR_DARK_GREEN);
    }
}
