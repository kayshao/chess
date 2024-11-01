package chess;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    public ChessGame.TeamColor color;
    public ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.color = pieceColor;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        ChessPiece that = (ChessPiece) o;
        return color == that.color && type == that.type;
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + color +
                ", type=" + type +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, type);
    }
    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }
    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        PieceType piece = board.getPiece(myPosition).getPieceType();
        if (piece == PieceType.KING) {
            int[][] kDirections = {{0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, -1}, {-1, 0}, {-1, 1}};
            KingMovesCalculator king = new KingMovesCalculator();
            return king.kingMoves(board, myPosition, kDirections);
        }
        if (piece == PieceType.QUEEN) {
            int[][] qDirections = {{0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, -1}, {-1, 0}, {-1, 1}};
            QueenMovesCalculator queen = new QueenMovesCalculator();
            return queen.queenMoves(board, myPosition, qDirections);
        }
        if (piece == PieceType.KNIGHT) {
            int[][] knDirections = {{1, 2}, {2, 1}, {2, -1}, {1, -2}, {-1, -2}, {-2, -1}, {-2, 1}, {-1, 2}};
            KnightMovesCalculator knight = new KnightMovesCalculator();
            return knight.knightMoves(board, myPosition, knDirections);
        }
        else if (piece == PieceType.BISHOP) {
            int[][] bDirections = {{1,1}, {1, -1}, {-1, -1}, {-1, 1}};
            BishopMovesCalculator bishop = new BishopMovesCalculator();
            return bishop.bishopMoves(board, myPosition, bDirections);
        }
        else if (piece == PieceType.ROOK) {
            int[][] rDirections = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
            RookMovesCalculator rook = new RookMovesCalculator();
            return rook.rookMoves(board, myPosition, rDirections);
        }
        else if (piece == PieceType.PAWN) {
            int[][] pDirections = {{1,0}, {-1,0}};
            PawnMovesCalculator pawn = new PawnMovesCalculator();
            return pawn.pawnMoves(board, myPosition, pDirections);
        }
        else {return null;}
    }
}
