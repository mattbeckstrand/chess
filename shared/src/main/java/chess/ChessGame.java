package chess;

import java.util.*;

/**
 *  * For a class that can manage a chess game, making moves on a board
 *  * <p>
 *  * Note: You can add to this class, but you may not alter
 *  * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard board;

    public ChessGame() {
        board = new ChessBoard();
        setBoard(board);

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        throw new RuntimeException("Not implemented");
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
        System.out.println(startPosition.getRow());
        System.out.println(startPosition.getColumn());
        if (board.getPiece(startPosition) == null) {
            throw new IllegalStateException("no piece found at start position");
        }
        ChessPiece piece = board.getPiece(startPosition);
        return piece.pieceMoves(board, startPosition);
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
//        ChessPosition kingPos = getKing(board, teamColor);
//        int i = 1;
//        int j = 1;
//        while (i < 9){
//            while (j < 9){
//                ChessPosition position = new ChessPosition(i,j);
//                j++;
//                if (board.getPiece(position) != null && board){
//
//                }
//
//            }
//            i++;
//        }
        throw new RuntimeException("Not implemented");
    }


    public ChessPosition getKing(ChessBoard board, TeamColor teamColor) {
        int i = 1;
        int j = 1;
        while (i < 9){
            j = 1;
            while (j < 9){
                ChessPosition position = new ChessPosition(i,j);
                j++;
                if (board.getPiece(position).getPieceType() != null && board.getPiece(position).getPieceType() == ChessPiece.PieceType.KING && board.getPiece(position).getTeamColor() == teamColor){
                    return position;
                }
            }
            i++;
        }
        throw new IllegalStateException("No king found for team: " + teamColor);
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
        board.resetBoard();
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }
}
