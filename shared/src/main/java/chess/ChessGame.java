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
    private TeamColor moveColor;
    public ChessGame() {
        board = new ChessBoard();
        setBoard(board);
        moveColor = TeamColor.WHITE;

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return this.moveColor;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.moveColor = team;
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
        ChessPiece piece = board.getPiece(startPosition);
        TeamColor tcolor = piece.getTeamColor();
        System.out.println(startPosition.getRow());
        System.out.println(startPosition.getColumn());
        System.out.println(tcolor);
        Collection<ChessMove> moves = piece.pieceMoves(board, startPosition);
        Collection<ChessMove> toReturnMoves = new ArrayList<>();
        ChessBoard ogBoard = board.clone();
        for (ChessMove move : moves) {
            try {
                this.board = ogBoard.clone();
                this.makeMove(move);
                boolean kingCheck = this.isInCheck(tcolor);
                if (!kingCheck) {
                    toReturnMoves.add(move);
                }
            } catch (InvalidMoveException e) {
                continue;
            }
        }
        this.board = ogBoard;
        List<List<Integer>> intMoves = new ArrayList<>();
        for (ChessMove move: toReturnMoves){
            List<Integer> oneAdd = new ArrayList<>();
            int row = move.getEndPosition().getRow();
            int col = move.getEndPosition().getColumn();
            oneAdd.add(row);
            oneAdd.add(col);
            intMoves.add(oneAdd);
        }
        System.out.println(intMoves);
        return toReturnMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition startPosition = move.getStartPosition();
        ChessPosition endPosition = move.getEndPosition();
        ChessPiece.PieceType promPiece = move.getPromotionPiece();
        TeamColor color = board.getPiece(startPosition).getTeamColor();
        ChessPiece.PieceType type = board.getPiece(startPosition).getPieceType();
        this.board.removePiece(startPosition);
        ChessPiece newPiece;
        if (promPiece == null){
            newPiece = new ChessPiece(color, type);
        }
        else {
            newPiece = new ChessPiece(color, promPiece);
        }
        this.board.addPiece(endPosition, newPiece);

    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        System.out.println(teamColor);
        ChessPosition kingPos = getKing(teamColor);
        int kingRow = kingPos.getRow();
        int kingColumn = kingPos.getColumn();
        int i = 1;
        int j;
        while (i < 9){
            j = 1;
            while (j < 9){
                ChessPosition position = new ChessPosition(i,j);
                ChessPiece piece = board.getPiece(position);
                if (piece != null && piece.getTeamColor() != teamColor){
                    Collection<ChessMove> moves = piece.pieceMoves(board, position);
                    for (ChessMove move : moves) {
                        ChessPosition endPosition = move.getEndPosition();
                        if (endPosition.getRow() == kingRow && endPosition.getColumn() == kingColumn) {
                            return true;
                        }
                    }
                }
                j++;
            }
            i++;

        }
        return false;
    }


    public ChessPosition getKing(TeamColor teamColor) {
        System.out.println(teamColor);
        int i = 1;
        int j;
        while (i < 9){
            j = 1;
            while (j < 9){
                ChessPosition position = new ChessPosition(i,j);
                if ( board.getPiece(position) != null && board.getPiece(position).getPieceType() == ChessPiece.PieceType.KING && board.getPiece(position).getTeamColor() == teamColor){
                    System.out.println("king");
                    System.out.println(i);
                    System.out.println(j);
                    TeamColor color = board.getPiece(position).getTeamColor();
                    System.out.println(color);
                    return position;
                }
                j++;
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
