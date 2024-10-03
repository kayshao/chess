package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    public ChessBoard board = new ChessBoard();

    public ChessGame() {
        board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {

        return TeamColor.WHITE;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        TeamColor otherTeam = TeamColor.BLACK;
        if (teamColor == TeamColor.BLACK) {otherTeam = TeamColor.WHITE;}

        ChessPosition kingPos = getKing(teamColor);
        Collection<ChessPosition> piecePositions = getPiecePositions(otherTeam);
        for (ChessPosition piecePosition : piecePositions) {
            Collection<ChessMove> moves = ChessPiece.pieceMoves(board, piecePosition);
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
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;}

    /**
     * Gets the positions of all pieces of one color
     *
     * @param color which team to get positions of
     * @return collection of ChessPositions
     */
    private Collection<ChessPosition>getPiecePositions(TeamColor color) {
        List<ChessPosition> piecePositions = new ArrayList<>();
        for(int i=0; i<8; i++) {
            for (int j=0; j<8; j++) {
                if (board.squares[i][j] != null) {
                    if (board.squares[i][j].getTeamColor() == color) {
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
    private ChessPosition getKing(TeamColor color) {
        for (int i=0; i<8; i++) {
            for (int j=0; j<8; j++) {
                ChessPiece position = board.squares[i][j];
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
