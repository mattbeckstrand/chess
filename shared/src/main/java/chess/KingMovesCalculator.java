package chess;

import java.util.*;

import chess.*;

public class KingMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        int[][] directions = {
                {1, 1},
                {1, 0},
                {-1, 0},
                {0, 1},
                {0, -1},
                {-1, 1},
                {-1, -1},
                {1, -1}
        };

        GenerateMovesInDirection genMoves = new GenerateMovesInDirection(board, position, directions);

        return genMoves.generateKingKnightMoves();
    }
}