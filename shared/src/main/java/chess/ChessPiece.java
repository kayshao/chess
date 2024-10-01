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
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        // create directions for each piece
        int[][] k_q_directions = {{0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, -1}, {-1, 0}, {-1, 1}};
        int[][] kn_directions = {{1, 2}, {2, 1}, {2, -1}, {1, -2}, {-1, -2}, {-2, -1}, {-2, 1}, {-1, 2}};
        int[][] b_directions = {{1,1}, {1, -1}, {-1, -1}, {-1, 1}};
        int[][] r_directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        int[][] p_directions = {{1,0}, {-1,0}};

        if (type == PieceType.KING) {
            KingMovesCalculator king = new KingMovesCalculator();
            return king.kingMoves(board, myPosition, k_q_directions);
        }
        else if (type == PieceType.QUEEN) {
            QueenMovesCalculator queen = new QueenMovesCalculator();
            return queen.queenMoves(board, myPosition, k_q_directions);
        }
        else if (type == PieceType.KNIGHT) {
            KnightMovesCalculator knight = new KnightMovesCalculator();
            return knight.knightMoves(board, myPosition, kn_directions);
        }
        else if (type == PieceType.BISHOP) {
            BishopMovesCalculator bishop = new BishopMovesCalculator();
            return bishop.bishopMoves(board, myPosition, b_directions);
        }
        else if (type == PieceType.ROOK) {
            RookMovesCalculator rook = new RookMovesCalculator();
            return rook.rookMoves(board, myPosition, r_directions);
        }
        else {
            PawnMovesCalculator pawn = new PawnMovesCalculator();
            return pawn.pawnMoves(board, myPosition, p_directions);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return color == that.color && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, type);
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "color=" + color +
                ", type=" + type +
                '}';
    }
}
