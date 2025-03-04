package chess;

import java.util.*;

import chess.ChessPosition;
import chess.ChessPiece;

public class KnightMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        int[][] directions = {

                {2, 1},
                {2, -1},
                {-2, 1},
                {-2, -1},
                {1, 2},
                {1, -2},
                {-1, 2},
                {-1, -2}
        };
        GenerateMovesInDirection genMoves = new GenerateMovesInDirection(board, position, directions);

        return genMoves.generateKingKnightMoves();
    }
}
