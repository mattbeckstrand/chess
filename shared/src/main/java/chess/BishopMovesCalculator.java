package chess;

import java.util.ArrayList;
import java.util.Collection;
import chess.ChessPosition;

public class BishopMovesCalculator implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();

        int row = position.getRow();
        int col = position.getColumn();





        return new ArrayList<>();
    }
}
