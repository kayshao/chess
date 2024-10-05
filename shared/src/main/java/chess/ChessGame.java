package chess;

import java.util.*;


/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor turn;
    public ChessBoard board = new ChessBoard();

    public ChessGame() {
        board.resetBoard();
        this.turn = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.turn = team;
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
        if (board.getPiece(startPosition) == null) {
            return null;
        }

        Collection<ChessMove> allMoves = ChessPiece.pieceMoves(board, startPosition);
        Set<ChessMove> validMoves = new HashSet<>();
        TeamColor color = board.squares[startPosition.getRow() - 1][startPosition.getColumn() - 1].getTeamColor();

        if (allMoves != null) {
            for (ChessMove move : allMoves) {
                ChessBoard boardCopy = board.copyBoard();
                boardCopy.movePiece(startPosition, move.getEndPosition());
                if (!boardCopy.boardIsInCheck(color)) {
                    validMoves.add(move);
                }
            }
        }
        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if (board.getPiece(move.getStartPosition()) == null) {
            throw new InvalidMoveException();
        }
        if (!validMoves(move.getStartPosition()).contains(move) | turn != board.getPiece(move.getStartPosition()).getTeamColor()) {
            throw new InvalidMoveException();
        }
        else {
            board.movePiece(move.getStartPosition(), move.getEndPosition());
        }

    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        return board.boardIsInCheck(teamColor);
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) {
            return false;
        }
        else {
            Collection<ChessPosition> piecePositions = board.getPiecePositions(teamColor);
            for (ChessPosition position : piecePositions) {
                if (!validMoves(position).isEmpty()) {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor)) {
            return false;
        }
        else {
            Collection<ChessPosition> piecePositions = board.getPiecePositions(teamColor);
            for (ChessPosition position : piecePositions) {
                if (!validMoves(position).isEmpty()) {
                    return false;
                }
            }
            return true;
        }
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

}
