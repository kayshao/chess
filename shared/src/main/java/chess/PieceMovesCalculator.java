package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public abstract class PieceMovesCalculator {
    protected HashSet<ChessMove> moves;

    public PieceMovesCalculator() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PieceMovesCalculator that = (PieceMovesCalculator) o;
        return moves.size() == that.moves.size() && moves.containsAll(that.moves);
    }

    @Override
    public int hashCode() {
        return moves.stream().mapToInt(ChessMove::hashCode).sum();
    }

}

class KingMovesCalculator extends PieceMovesCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        return new ArrayList<>();
    }
}

class BishopMovesCalculator extends PieceMovesCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> moves = new HashSet<>();
        ChessGame.TeamColor b_color =  board.getPiece(myPosition).getTeamColor();       // get team color of Bishop
        int[][] combinations = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
        for (int[] combination : combinations) {
            int k = combination[0];
            int l = combination[1];
            int j = myPosition.getColumn();

            for (int i = myPosition.getRow()+k; i < 9; i+=k) {
                j+=l;
                if (j == 9 | i == 0 | j == 0) {
                    break; }
                ChessPosition possiblePos = new ChessPosition(i, j);
                if (board.getPiece(possiblePos) == null) {
                        moves.add(new ChessMove(myPosition, possiblePos, null));
                }
                else if (board.getPiece(possiblePos).getTeamColor() != b_color) {
                    moves.add(new ChessMove(myPosition, possiblePos, null));
                    break;
                }
                else break;
            }
        }
        return moves;
    }
}