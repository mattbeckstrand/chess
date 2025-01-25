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
        System.out.println(startPosition);
        //get piece and check the team color, used to validate moves
        ChessPiece startingPiece = board.getPiece(startPosition);
        ChessGame.TeamColor tColor = startingPiece.getTeamColor();
        System.out.println(tColor);
        ArrayList<ChessPosition> endPositionTest = new ArrayList<ChessPosition>();
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        List<List<Integer>> movesList = new ArrayList<>();
        int i = 1;

        //All possible positions
        ChessPosition endPosition1 = new ChessPosition((row + 1), (col));
        ChessPosition endPosition2 = new ChessPosition((row), (col + 1));
        ChessPosition endPosition3 = new ChessPosition((row + 1), (col + 1));
        ChessPosition endPosition4 = new ChessPosition((row - 1), (col - 1));
        ChessPosition endPosition5 = new ChessPosition((row - 1), (col + 1));
        ChessPosition endPosition6 = new ChessPosition((row + 1), (col - 1));
        ChessPosition endPosition7 = new ChessPosition((row - 1), (col));
        ChessPosition endPosition8 = new ChessPosition((row), (col - 1));

        //have positions in a list
        endPositionTest.add(endPosition1);
        endPositionTest.add(endPosition2);
        endPositionTest.add(endPosition3);
        endPositionTest.add(endPosition4);
        endPositionTest.add(endPosition5);
        endPositionTest.add(endPosition6);
        endPositionTest.add(endPosition7);
        endPositionTest.add(endPosition8);


        System.out.println("starting King");
        for (ChessPosition cP : endPositionTest) {
            col = cP.getColumn();
            row = cP.getRow();
            System.out.println(col);
            System.out.println(row);
            //first check: is the counter in bounds
            if (col > 8 || row > 8 || col <= 0 || row <= 0) {
                break;
            }
            //second check: if it is in bounds and the position has no piece, add it
            else if (board.getPiece(cP) == null) {
                ChessMove move = new ChessMove(startPosition, cP, null);
                moves.add(move);
                List<Integer> lOfMoves = new ArrayList<Integer>();
                lOfMoves.add(row + i);
                lOfMoves.add(col + i);
                movesList.add(lOfMoves);
            }
            //third check: we already know that it is null, just check if it is the same team color, if it is then skip
            else if(board.getPiece(cP).getTeamColor() == tColor)
            {
                continue;
            }
            //fourth check: if the team color isn't equal to ours, add the move
            else if(board.getPiece(cP).getTeamColor() != tColor){
                ChessMove move = new ChessMove(startPosition, cP, null);
                moves.add(move);
                List<Integer> lOfMoves = new ArrayList<Integer>();
                lOfMoves.add(row + i);
                lOfMoves.add(col + i);
                movesList.add(lOfMoves);
            }
        }
        System.out.println(movesList);

        return moves;
    }
}