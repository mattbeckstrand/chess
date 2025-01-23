package chess;

import java.util.*;

import chess.*;

public class KingMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        //need start position, end position and promotion piece
        int row = position.getRow();
        int col = position.getColumn();
        ChessPosition startPosition = new ChessPosition(row, col);
        //get piece and check the team color, used to validate moves
        ChessPiece startingPiece = board.getPiece(startPosition);
        ChessGame.TeamColor tColor = startingPiece.getTeamColor();
        ArrayList<ChessMove> movesTest = new ArrayList<ChessMove>();
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        List<List<Integer>> movesList = new ArrayList<>();
        int i = 1;


        ChessPosition endPosition1 = new ChessPosition((row + 1), (col));
        ChessPosition endPosition2 = new ChessPosition((row), (col + 1));
        ChessPosition endPosition3 = new ChessPosition((row + 1), (col + 1));
        ChessPosition endPosition4 = new ChessPosition((row - 1), (col - 1));
        ChessPosition endPosition5 = new ChessPosition((row - 1), (col + 1));
        ChessPosition endPosition6 = new ChessPosition((row + 1), (col - 1));
        ChessPosition endPosition7 = new ChessPosition((row - 1), (col));
        ChessPosition endPosition8 = new ChessPosition((row), (col - 1));


        movesTest.add(endP)




        //moving up and to the right
        while ((row + i) <= 8 && (col + i) <= 8) {
            ChessPosition endPosition = new ChessPosition((row + i), (col + i));
            if (board.getPiece(endPosition) == null) {
                ChessMove move = new ChessMove(startPosition, endPosition, null);
                moves.add(move);
                List<Integer> lOfMoves = new ArrayList<Integer>();
                lOfMoves.add(row + i);
                lOfMoves.add(col + i);
                movesList.add(lOfMoves);
                ++i;
            } else if(board.getPiece(endPosition) != null && board.getPiece(endPosition).getTeamColor() == tColor)
            {
                break;
            } else if(board.getPiece(endPosition) != null && board.getPiece(endPosition).getTeamColor() != tColor){
                ChessMove move = new ChessMove(startPosition, endPosition, null);
                moves.add(move);
                List<Integer> lOfMoves = new ArrayList<Integer>();
                lOfMoves.add(row + i);
                lOfMoves.add(col + i);
                movesList.add(lOfMoves);
                break;
            }
        }
        System.out.println(movesList);

        return moves;
    }
}