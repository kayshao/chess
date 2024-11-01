package chess;

import java.util.*;
import java.util.stream.Stream;

public abstract class PieceMovesCalculator {
    public Collection<ChessMove> moves = new HashSet<>();
    public HashSet<ChessMove> allPieceMoves(ChessBoard board, ChessPosition start, int[][] directions) {
        HashSet<ChessMove> moves = new HashSet<>();
        ChessGame.TeamColor color = board.getPiece(start).getTeamColor();

        for (int[] d : directions) {
            int i = start.getRow();
            int j = start.getColumn();
            for (i = i + d[0]; i < 9; i += d[0]) {
                j = j + d[1];
                if ( i < 1 | j > 8 | j < 1) {break;}
                ChessPosition possibleEnd = new ChessPosition(i, j);
                if (board.getPiece(possibleEnd) == null) {
                    moves.add(new ChessMove(start, possibleEnd, null));
                }
                else if (board.getPiece(possibleEnd).getTeamColor() != color) {
                    moves.add(new ChessMove(start, possibleEnd, null));
                    break;
                }
                else {break;}
            }
        }
        return moves;
    }
    public HashSet<ChessMove> knightKingMoves(ChessBoard board, ChessPosition start, int[][]directions) {
        HashSet<ChessMove> moves = new HashSet<>();
        ChessGame.TeamColor color = board.getPiece(start).getTeamColor();

        for (int[] d : directions) {
            int i = start.getRow();
            int j = start.getColumn();
            if (i + d[0] < 9 && i + d[0] > 0 && j + d[1] < 9 && j + d[1] > 0) {
                i += d[0];
                j += d[1];

                ChessPosition possibleEnd = new ChessPosition(i, j);
                if (board.getPiece(possibleEnd) == null) {
                    moves.add(new ChessMove(start, possibleEnd, null));
                }
                else if (board.getPiece(possibleEnd).getTeamColor() != color) {
                    moves.add(new ChessMove(start, possibleEnd, null));
                }
            }
        }
        return moves;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        PieceMovesCalculator that = (PieceMovesCalculator) o;
        return Objects.deepEquals(moves, that.moves);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(moves);
    }
}
class KingMovesCalculator extends PieceMovesCalculator {
    public Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition start, int[][] directions) {
        return knightKingMoves(board, start, directions);
    }
}
class QueenMovesCalculator extends PieceMovesCalculator {
    public HashSet<ChessMove> queenMoves(ChessBoard board, ChessPosition start, int[][] directions) {
        return allPieceMoves(board, start, directions);
    }
}
class KnightMovesCalculator extends PieceMovesCalculator {
    public Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition start, int[][] directions) {
        return knightKingMoves(board, start, directions);
    }
}
class BishopMovesCalculator extends PieceMovesCalculator {
    public Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition start, int[][] directions) {
        return allPieceMoves(board, start, directions);
    }
}
class RookMovesCalculator extends PieceMovesCalculator {
    public Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition start, int[][] directions) {
        return allPieceMoves(board, start, directions);
    }
}
class PawnMovesCalculator extends PieceMovesCalculator {
    private void addForwardMove(HashSet<ChessMove> moves, ChessBoard board, int i, int j, int d, ChessGame.TeamColor color) {
        ChessPosition possibleEnd = new ChessPosition(i + d, j);
        ChessPosition start = new ChessPosition(i, j);
        if (board.getPiece(possibleEnd) == null) {
            if ((color == ChessGame.TeamColor.BLACK && i == 2) | (color == ChessGame.TeamColor.WHITE && i == 7)) {
                addPromotion(moves, start, possibleEnd);
            } else {
                moves.add(new ChessMove(start, possibleEnd, null));
                if ((color == ChessGame.TeamColor.BLACK && i == 7) | (color == ChessGame.TeamColor.WHITE && i == 2)) {
                    ChessPosition possibleEnd2 = new ChessPosition(i + 2 * d, j);
                    if (board.getPiece(possibleEnd2) == null) {
                        moves.add(new ChessMove(new ChessPosition(i, j), possibleEnd2, null));
                    }
                }
            }
        }
    }

    private void addPromotion(HashSet<ChessMove> moves, ChessPosition start, ChessPosition possiblePos) {
        moves.add(new ChessMove(start, possiblePos, ChessPiece.PieceType.KNIGHT));
        moves.add(new ChessMove(start, possiblePos, ChessPiece.PieceType.QUEEN));
        moves.add(new ChessMove(start, possiblePos, ChessPiece.PieceType.BISHOP));
        moves.add(new ChessMove(start, possiblePos, ChessPiece.PieceType.ROOK));
    }

    private void addAttack(HashSet<ChessMove> moves, ChessBoard board, int i, int j, int d, ChessGame.TeamColor color) {
        ChessPosition start = new ChessPosition(i,j);
        List<Integer> cs = new ArrayList<>();
        if (j < 8) { cs.add(1); }
        if (j > 1) { cs.add(-1); }
        for (int c: cs) {
            ChessPosition possibleAttack = new ChessPosition(i + d, j + c);
            if (board.getPiece(possibleAttack) != null) {
                if (board.getPiece(possibleAttack).getTeamColor() != color) {
                    if ((color == ChessGame.TeamColor.BLACK && i == 2) | (color == ChessGame.TeamColor.WHITE && i == 7)) {
                        addPromotion(moves, start, possibleAttack);
                    }
                    else {
                        moves.add(new ChessMove(start, possibleAttack, null));
                    }
                }
            }
        }
    }


    public Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition start, int[][] directions) {
        HashSet<ChessMove> moves = new HashSet<>();
        ChessGame.TeamColor color = board.getPiece(start).getTeamColor();
        int i = start.getRow();
        int j = start.getColumn();
        //black piece
        if (color == ChessGame.TeamColor.BLACK) {
            addForwardMove(moves, board, i, j, -1, ChessGame.TeamColor.BLACK);
            addAttack(moves, board, i, j, -1, ChessGame.TeamColor.BLACK);
        }
        // white moves
        else {
            addForwardMove(moves, board, i, j, 1, ChessGame.TeamColor.WHITE);
            addAttack(moves, board, i, j, 1, ChessGame.TeamColor.WHITE);
        }
        return moves;
    }
}
