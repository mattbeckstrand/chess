package chess;

import java.util.ArrayList;
import java.util.Collection;
import chess.ChessPosition;

public class BishopMovesCalculator implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        //need start position, end position and promotion piece
        int row = position.getRow();
        int col = position.getColumn();
        ChessPosition startPosition = new ChessPosition(row, col);
        //start position will stay the same
        //end position will need to be changed in a loop to all possible end positions. promotion will always be null for bishop
        ChessPosition endPosition = new ChessPosition(row + 1, col +1 );

        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        ChessMove move = new ChessMove(startPosition, endPosition, null);
        moves.add(move);

        return moves;
    }
}
