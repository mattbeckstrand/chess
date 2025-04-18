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
        board.resetBoard();
        moveColor = TeamColor.WHITE;

    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(board, chessGame.board) && moveColor == chessGame.moveColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, moveColor);
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
        ChessPiece startPiece = board.getPiece(startPosition);
        TeamColor tcolor = startPiece.getTeamColor();
        Collection<ChessMove> moves = startPiece.pieceMoves(board, startPosition);
        Collection<ChessMove> toReturnMoves = new ArrayList<>();
        for (ChessMove move : moves) {
                ChessPosition endPosition = move.getEndPosition();
                ChessPiece endPiece = board.getPiece(endPosition);
                this.board.removePiece(startPosition);
                this.board.addPiece(endPosition, startPiece);
                int erow = endPosition.getRow();
                int ecol = endPosition.getColumn();
                boolean kingCheck = this.isInCheck(tcolor);
                if (!kingCheck) {
                    toReturnMoves.add(move);
                }
                board.addPiece(endPosition, endPiece);
                board.addPiece(startPosition, startPiece);

        }
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
        boolean found = false;
        ChessPosition endPosition = move.getEndPosition();
        if(board.getPiece(startPosition) == null || board.getPiece(startPosition).getTeamColor() != this.getTeamTurn()){
            throw new InvalidMoveException("Error: invalid move");
        }
        Collection<ChessMove> validMoves = validMoves(startPosition);

        for (ChessMove vmove: validMoves){
            if (vmove.getEndPosition().equals(endPosition)){
                found = true;
                break;
            }
        }
        if (!found){
            throw new InvalidMoveException("Error: invalid move");
        }
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
        if (this.getTeamTurn() == TeamColor.BLACK){
            setTeamTurn(TeamColor.WHITE);
        }
        else {
            setTeamTurn(TeamColor.BLACK);
        }
    }

    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPos = getKing(teamColor);
        int kingRow = kingPos.getRow();
        int kingColumn = kingPos.getColumn();
        int i = 1;
        int j;

        while (i < 9) {
            j = 1;
            while (j < 9) {
                ChessPosition position = new ChessPosition(i, j);
                ChessPiece piece = board.getPiece(position);

                if (piece == null || piece.getTeamColor() == teamColor) {
                    j++;
                    continue;
                }

                Collection<ChessMove> moves = piece.pieceMoves(board, position);
                for (ChessMove move : moves) {
                    ChessPosition endPosition = move.getEndPosition();
                    if (endPosition.getRow() == kingRow && endPosition.getColumn() == kingColumn) {
                        return true;
                    }
                }
                j++;
            }
            i++;
        }
        return false;
    }


    public ChessPosition getKing(TeamColor teamColor) {
        int i = 1;
        int j;
        while (i < 9){
            j = 1;
            while (j < 9){
                ChessPosition position = new ChessPosition(i,j);
                if ( board.getPiece(position) != null
                        && board.getPiece(position).getPieceType() == ChessPiece.PieceType.KING
                        && board.getPiece(position).getTeamColor() == teamColor){
                    return position;
                }
                j++;
            }
            i++;
        }
        throw new IllegalStateException("No king found for team: " + teamColor);
    }

    public boolean isInCheckmate(TeamColor teamColor) {
        Collection<ChessPosition> teamPositions = this.getTeamPositions(teamColor);
        Collection<ChessMove> comp = new ArrayList<>();
        if (isInCheck(teamColor)){
            for (ChessPosition position : teamPositions){
                if (!this.validMoves(position).isEmpty()){
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public boolean isInStalemate(TeamColor teamColor) {
        Collection<ChessPosition> teamPositions = this.getTeamPositions(teamColor);
        if (isInCheck(teamColor)){
            return false;
        }
        for (ChessPosition position : teamPositions){
            if (!this.validMoves(position).isEmpty()){
                return false;
            }
        }

        return true;
    }

    public Collection<ChessPosition> getTeamPositions(TeamColor teamColor){
        Collection<ChessPosition> positionList = new ArrayList<>();
        System.out.println(teamColor);
        int i = 1;
        int j;
        while (i < 9){
            j = 1;
            while (j < 9){
                ChessPosition position = new ChessPosition(i,j);
                if ( board.getPiece(position) != null && board.getPiece(position).getTeamColor() == teamColor){
                    positionList.add(position);
                }
                j++;
            }
            i++;
        }
        return positionList;

    }

    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    public ChessBoard getBoard() {
        return this.board;
    }
}
