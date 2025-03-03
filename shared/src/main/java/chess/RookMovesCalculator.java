package chess;

import java.util.*;

import chess.ChessPosition;
import chess.ChessPiece;

public class RookMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        int[][] directions = {
                {1, 0},
                {-1, 0},
                {0, 1},
                {0, -1}
        };
        GenerateMovesInDirection genMoves = new GenerateMovesInDirection(board, position, directions);

        return genMoves.callGenMoves();
    }
}