package chess;

import java.util.*;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    public final ChessPiece[][] squares = new ChessPiece[8][8];

    public ChessBoard() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("  1 2 3 4 5 6 7 8\n");

        for (int row = 7; row >= 0; row--) {
            sb.append(row + 1).append(" ");
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = squares[row][col];
                if (piece == null) {
                    sb.append(((row + col) % 2 == 0) ? "□ " : "■ ");
                } else {
                    sb.append(pieceToChar(piece)).append(" ");
                }
            }
            sb.append(row + 1).append("\n");
        }

        sb.append("  1 2 3 4 5 6 7 8");
        return sb.toString();
    }

    private char pieceToChar(ChessPiece piece) {
        char c = switch (piece.getPieceType()) {
            case KING -> 'K';
            case QUEEN -> 'Q';
            case ROOK -> 'R';
            case BISHOP -> 'B';
            case KNIGHT -> 'N';
            case PAWN -> 'P';
        };
        return piece.getTeamColor() == ChessGame.TeamColor.WHITE ? c : Character.toLowerCase(c);
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow() - 1][position.getColumn() - 1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow() - 1][position.getColumn() - 1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        for (int i = 1; i < 9; i++) {
            addPiece(new ChessPosition(2, i), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
            addPiece(new ChessPosition(7, i), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        }
        addPiece(new ChessPosition(1, 1), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(1, 2), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(1, 3), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(1, 4), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));
        addPiece(new ChessPosition(1, 5), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));
        addPiece(new ChessPosition(1, 6), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(1, 7), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(1, 8), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(8, 1), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(8, 2), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(8, 3), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(8, 4), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN));
        addPiece(new ChessPosition(8, 5), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));
        addPiece(new ChessPosition(8, 6), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(8, 7), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(8, 8), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
    }

    public ChessBoard copyBoard() {
        ChessBoard boardCopy = new ChessBoard();
        for (int i = 0; i < 8; i++) {
            System.arraycopy(squares[i], 0, boardCopy.squares[i], 0, 8);
        }
        return boardCopy;
    }

    public void movePiece(ChessPosition startPosition, ChessPosition endPosition) {
        ChessPiece piece = this.getPiece(startPosition);
        this.addPiece(endPosition, piece);
        this.addPiece(startPosition, null);
    }

    public boolean boardIsInCheck(ChessGame.TeamColor teamColor) {
        ChessGame.TeamColor otherTeam = ChessGame.TeamColor.BLACK;
        if (teamColor == ChessGame.TeamColor.BLACK) {otherTeam = ChessGame.TeamColor.WHITE;}

        ChessPosition kingPos = getKing(teamColor);
        Collection<ChessPosition> piecePositions = getPiecePositions(otherTeam);
        for (ChessPosition piecePosition : piecePositions) {
            Collection<ChessMove> moves = ChessPiece.pieceMoves(this, piecePosition);
            if (moves != null) {
                for (ChessMove move : moves) {
                    ChessPosition pos = move.getEndPosition();
                    if (pos.equals(kingPos)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Gets the positions of all pieces of one color
     *
     * @param color which team to get positions of
     * @return collection of ChessPositions
     */
    public Collection<ChessPosition>getPiecePositions(ChessGame.TeamColor color) {
        List<ChessPosition> piecePositions = new ArrayList<>();
        for(int i=0; i<8; i++) {
            for (int j=0; j<8; j++) {
                if (this.squares[i][j] != null) {
                    if (this.squares[i][j].getTeamColor() == color) {
                        piecePositions.add(new ChessPosition(i+1, j+1));
                    }
                }
            }
        }
        return piecePositions;
    }

    /**
     * Gets the position of one team's king
     *
     * @param color which team to get king's position
     * @return position of king
     */
    private ChessPosition getKing(ChessGame.TeamColor color) {
        for (int i=0; i<8; i++) {
            for (int j=0; j<8; j++) {
                ChessPiece position = this.squares[i][j];
                if (position != null) {
                    if (position.getTeamColor() == color && position.getPieceType() == ChessPiece.PieceType.KING) {
                        return new ChessPosition(i+1, j+1);
                    }
                }
            }
        }
        return null;
    }
}
