package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
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
        HashSet<ChessMove> moves = new HashSet<>();
        ChessGame.TeamColor color = board.getPiece(start).getTeamColor();

        for (int[] d : directions) {
            int i = start.getRow();
            int j = start.getColumn();
            if (i < 8 && i > 1 && j < 8 && j > 1) {
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
}
class QueenMovesCalculator extends PieceMovesCalculator {
    public HashSet<ChessMove> queenMoves(ChessBoard board, ChessPosition start, int[][] directions) {
        return allPieceMoves(board, start, directions);
    }
}
class KnightMovesCalculator extends PieceMovesCalculator {
    public Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition start, int[][] directions) {
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
    public Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition start, int[][] directions) {
        HashSet<ChessMove> moves = new HashSet<>();
        ChessGame.TeamColor color = board.getPiece(start).getTeamColor();
        int i = start.getRow();
        int j = start.getColumn();
        int[] d = new int[2];
        if (color == ChessGame.TeamColor.BLACK) {
            d = directions[1];
            ChessPosition possibleEnd = new ChessPosition(i - 1, j);
            if (board.getPiece(possibleEnd) == null) {
                if (i == 2) {
                    moves.add(new ChessMove(start, possibleEnd, ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(start, possibleEnd, ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(start, possibleEnd, ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(start, possibleEnd, ChessPiece.PieceType.ROOK));
                } else {
                    moves.add(new ChessMove(start, possibleEnd, null));
                }
                if (i == 7) {
                    ChessPosition possibleEnd1 = new ChessPosition(i - 2, j);
                    if (board.getPiece(possibleEnd1) == null) {
                        moves.add(new ChessMove(start, possibleEnd1, null));
                    }
                }
            }
            if (j < 8) {
                ChessPosition possibleAttack1 = new ChessPosition(i - 1, j + 1);
                if (board.getPiece(possibleAttack1) != null) {
                    if (board.getPiece(possibleAttack1).getTeamColor() == ChessGame.TeamColor.WHITE) {
                        if (i == 2) {
                            moves.add(new ChessMove(start, possibleAttack1, ChessPiece.PieceType.KNIGHT));
                            moves.add(new ChessMove(start, possibleAttack1, ChessPiece.PieceType.QUEEN));
                            moves.add(new ChessMove(start, possibleAttack1, ChessPiece.PieceType.BISHOP));
                            moves.add(new ChessMove(start, possibleAttack1, ChessPiece.PieceType.ROOK));
                        } else {
                            moves.add(new ChessMove(start, possibleAttack1, null));
                        }
                    }
                }
            }
            if (j > 1) {
                ChessPosition possibleAttack2 = new ChessPosition(i - 1, j - 1);
                if (board.getPiece(possibleAttack2) != null) {
                    if (board.getPiece(possibleAttack2).getTeamColor() == ChessGame.TeamColor.WHITE) {
                        if (i == 2) {
                            moves.add(new ChessMove(start, possibleAttack2, ChessPiece.PieceType.KNIGHT));
                            moves.add(new ChessMove(start, possibleAttack2, ChessPiece.PieceType.QUEEN));
                            moves.add(new ChessMove(start, possibleAttack2, ChessPiece.PieceType.BISHOP));
                            moves.add(new ChessMove(start, possibleAttack2, ChessPiece.PieceType.ROOK));
                        } else {
                            moves.add(new ChessMove(start, possibleAttack2, null));
                        }
                    }
                }
            }
        }
        else {
            d = directions[0];
            ChessPosition possible = new ChessPosition(i + 1, j);
            if (board.getPiece(possible) == null) {
                if (i == 7) {
                    moves.add(new ChessMove(start, possible, ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(start, possible, ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(start, possible, ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(start, possible, ChessPiece.PieceType.ROOK));
                } else {
                    moves.add(new ChessMove(start, possible, null));
                }
                if (i == 2) {
                    ChessPosition possibleEnd1 = new ChessPosition(i + 2, j);
                    if (board.getPiece(possibleEnd1) == null) {
                        moves.add(new ChessMove(start, possibleEnd1, null));
                    }
                }
            }
            if (j < 8) {
                ChessPosition possibleAttack1 = new ChessPosition(i + 1, j + 1);
                if (board.getPiece(possibleAttack1) != null) {
                    if (board.getPiece(possibleAttack1).getTeamColor() == ChessGame.TeamColor.BLACK) {
                        if (i == 7) {
                            moves.add(new ChessMove(start, possibleAttack1, ChessPiece.PieceType.KNIGHT));
                            moves.add(new ChessMove(start, possibleAttack1, ChessPiece.PieceType.QUEEN));
                            moves.add(new ChessMove(start, possibleAttack1, ChessPiece.PieceType.BISHOP));
                            moves.add(new ChessMove(start, possibleAttack1, ChessPiece.PieceType.ROOK));
                        } else {
                            moves.add(new ChessMove(start, possibleAttack1, null));
                        }
                    }
                }
            }
            if (j > 1) {
                ChessPosition possibleAttack2 = new ChessPosition(i + 1, j - 1);
                if (board.getPiece(possibleAttack2) != null) {
                    if (board.getPiece(possibleAttack2).getTeamColor() == ChessGame.TeamColor.BLACK) {
                        if (i == 7) {
                            moves.add(new ChessMove(start, possibleAttack2, ChessPiece.PieceType.KNIGHT));
                            moves.add(new ChessMove(start, possibleAttack2, ChessPiece.PieceType.QUEEN));
                            moves.add(new ChessMove(start, possibleAttack2, ChessPiece.PieceType.BISHOP));
                            moves.add(new ChessMove(start, possibleAttack2, ChessPiece.PieceType.ROOK));
                        } else {
                            moves.add(new ChessMove(start, possibleAttack2, null));
                        }
                    }
                }
            }
        }
        return moves;
    }
}
