package chess;

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
        HashSet<ChessMove> moves = new HashSet<>();
        ChessGame.TeamColor k_color = board.getPiece(myPosition).getTeamColor();
        int[][] combinations = {{0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, -1}, {-1, 0}, {-1, 1}};
        int i = myPosition.getRow();
        int j = myPosition.getColumn();
        for (int[] combo : combinations) {
            if (i + combo[0] > 8 | j + combo[1] > 8 | i + combo[0] < 1 | j + combo[1] < 1) break;
            ChessPosition possiblePos = new ChessPosition(i+combo[0], j+combo[1]);
            if (board.getPiece(possiblePos) == null) {
                moves.add(new ChessMove(myPosition, possiblePos, null));
            }
            else if (board.getPiece(possiblePos).getTeamColor() != k_color) {
                moves.add(new ChessMove(myPosition, possiblePos, null));
            }
        }
        return moves;
    }
}

class QueenMovesCalculator extends PieceMovesCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> moves = new HashSet<>();
        ChessGame.TeamColor q_color = board.getPiece(myPosition).getTeamColor();
        int[][] combinations = {{-1, -1}, {-1, 0}, {-1, 1}, {0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}};
        for (int[] combo : combinations) {
            int k = combo[0];
            int l = combo[1];
            int j = myPosition.getColumn();
            for (int i = myPosition.getRow() + k; i < 9; i += k) {
                j += l;
                if (j == 9 | j == 0 | i == 0) break;
                ChessPosition possiblePos = new ChessPosition(i, j);
                if (board.getPiece(possiblePos) == null) {
                    moves.add(new ChessMove(myPosition, possiblePos, null));
                }
                else if (board.getPiece(possiblePos).getTeamColor() != q_color) {
                    moves.add(new ChessMove(myPosition, possiblePos, null));
                    break;
                }
                else break;
            }
        }
        return moves;
    }
}

class KnightMovesCalculator extends PieceMovesCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> moves = new HashSet<>();
        ChessGame.TeamColor k_color = board.getPiece(myPosition).getTeamColor();
        int[][] combinations = {{2, 1}, {1, 2}, {-1, 2}, {-2, 1}, {-2, -1}, {-1, -2}, {2, -1}, {1, -2}};
        int i = myPosition.getRow();
        int j = myPosition.getColumn();
        for (int[] combo : combinations) {
            if (i + combo[0] > 8 | j + combo[1] > 8 | i + combo[0] < 1 | j + combo[1] < 1) continue;
            ChessPosition possiblePos = new ChessPosition(i+combo[0], j+combo[1]);
            if (board.getPiece(possiblePos) == null) {
                moves.add(new ChessMove(myPosition, possiblePos, null));
            }
            else if (board.getPiece(possiblePos).getTeamColor() != k_color) {
                moves.add(new ChessMove(myPosition, possiblePos, null));
            }
        }
        return moves;
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

class RookMovesCalculator extends PieceMovesCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> moves = new HashSet<>();
        ChessGame.TeamColor r_color = board.getPiece(myPosition).getTeamColor();
        int[] nums = {1, -1};
        for (int k : nums) {
            int x = myPosition.getRow();
            int y = myPosition.getColumn();

            for (int i = myPosition.getRow() + k; i < 9; i += k) {
                if (i <= 0) {
                    break;
                }
                ChessPosition possiblePos = new ChessPosition(i, y);
                if (board.getPiece(possiblePos) == null) {
                    moves.add(new ChessMove(myPosition, possiblePos, null));
                }
                else if (board.getPiece(possiblePos).getTeamColor() != r_color) {
                    moves.add(new ChessMove(myPosition, possiblePos, null));
                    break;
                }
                else break;
            }
            for (int j = myPosition.getColumn() + k; j < 9; j += k) {
                if (j <= 0) {break;}
                ChessPosition possiblePos = new ChessPosition(x, j);
                if (board.getPiece(possiblePos) == null) {
                    moves.add(new ChessMove(myPosition, possiblePos, null));
                }
                else if (board.getPiece(possiblePos).getTeamColor() != r_color) {
                    moves.add(new ChessMove(myPosition, possiblePos, null));
                    break;
                }
                else break;
            }
        }
        return moves;
    }
}

class PawnMovesCalculator extends PieceMovesCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> moves = new HashSet<>();
        ChessGame.TeamColor p_color = board.getPiece(myPosition).getTeamColor();
        int i = myPosition.getRow();
        int j = myPosition.getColumn();

        if (p_color == ChessGame.TeamColor.WHITE) {
            if (i == 2) {
                int k = 2;
                ChessPosition possiblePos = new ChessPosition(i+k, j);
                if(board.getPiece(possiblePos) == null && board.getPiece(new ChessPosition(i+1, j)) == null) {
                    moves.add(new ChessMove(myPosition, possiblePos, null));
                }
            }
            int k = 1;
            ChessPosition possiblePos = new ChessPosition(i+k, j);
            if (board.getPiece(possiblePos) == null) {
                moves.add(new ChessMove(myPosition, possiblePos, null));
            }
            if (j < 8) {
                ChessPosition possible = new ChessPosition(i+k, j+1);
                if (board.getPiece(possible) != null) {
                    if (board.getPiece(possible).getTeamColor() != p_color) {
                        moves.add(new ChessMove(myPosition, possible, null));
                    }
                }
            }
            if (j > 1) {
                ChessPosition possible = new ChessPosition(i+k, j-1);
                if (board.getPiece(possible) != null) {
                    if (board.getPiece(possible).getTeamColor() != p_color) {
                        moves.add(new ChessMove(myPosition, possible, null));
                    }
                }
            }
        }
        else {
            if (i == 7) {
                int k = -2;
                ChessPosition possiblePos = new ChessPosition(i+k, j);
                if(board.getPiece(possiblePos) == null && board.getPiece(new ChessPosition(i-1, j)) == null) {
                    moves.add(new ChessMove(myPosition, possiblePos, null));
                }
            }
            ChessPosition possiblePos = new ChessPosition(i-1, j);
            if (board.getPiece(possiblePos) == null) {
                moves.add(new ChessMove(myPosition, possiblePos, null));
            }
            if (j < 8) {
                ChessPosition possible = new ChessPosition(i-1, j+1);
                if (board.getPiece(possible) != null) {
                    if (board.getPiece(possible).getTeamColor() != p_color) {
                        moves.add(new ChessMove(myPosition, possible, null));
                    }
                }
            }
            if (j > 1) {
                ChessPosition possible = new ChessPosition(i-1, j-1);
                if (board.getPiece(possible) != null) {
                    if (board.getPiece(possible).getTeamColor() != p_color) {
                        moves.add(new ChessMove(myPosition, possible, null));
                    }
                }
            }
        }
    return moves;}
}