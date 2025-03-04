package chess;

import java.util.*;

import chess.ChessPosition;
import chess.ChessPiece;

public class BishopMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        int[][] directions = {
                {1, 1},
                {-1, 1},
                {-1, -1},
                {1, -1}
        };
        GenerateMovesInDirection genMoves = new GenerateMovesInDirection(board, position, directions);

        return genMoves.callGenMovesBRQ();
    }
}
