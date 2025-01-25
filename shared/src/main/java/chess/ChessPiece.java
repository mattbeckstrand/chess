package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    private final ChessGame.TeamColor pieceColor;
    private final ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
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

    public PieceType getPieceType() {
        return type;
    }
    /**
     * @return Which team this chess piece belongs to
     */

    public enum TeamColor {
        WHITE,
        BLACK
    }
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */


    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        PieceMovesCalculator calculator;

        Collection<ChessMove> moves = null;
        if (this.getPieceType() == PieceType.BISHOP) {
            calculator = new BishopMovesCalculator();
            moves = calculator.pieceMoves(board, myPosition);
        }
        else if (this.getPieceType() == PieceType.KING) {
            calculator = new KingMovesCalculator();
            moves = calculator.pieceMoves(board, myPosition);
        }
        else if (this.getPieceType() == PieceType.KNIGHT) {
            calculator = new KnightMovesCalculator();
            moves = calculator.pieceMoves(board, myPosition);
        }
        else if (this.getPieceType() == PieceType.PAWN) {
            calculator = new PawnMovesCalculator();
            moves = calculator.pieceMoves(board, myPosition);
        }
        else if (this.getPieceType() == PieceType.QUEEN) {
            calculator = new QueenMovesCalculator();
            moves = calculator.pieceMoves(board, myPosition);
        }
        else if (this.getPieceType() == PieceType.ROOK) {
            calculator = new RookMovesCalculator();
            moves = calculator.pieceMoves(board, myPosition);
        }
        return moves;
    }
}
