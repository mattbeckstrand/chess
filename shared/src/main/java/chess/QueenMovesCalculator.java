package chess;

import java.util.*;

import chess.ChessPosition;
import chess.ChessPiece;

public class QueenMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        //need start position, end position and promotion piece
        int row = position.getRow();
        int col = position.getColumn();
        ChessPosition startPosition = new ChessPosition(row, col);
        //get piece and check the team color, used to validate moves
        ChessPiece startingPiece = board.getPiece(startPosition);
        ChessGame.TeamColor tColor = startingPiece.getTeamColor();
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        List<List<Integer>> movesList = new ArrayList<>();
        int i = 1;

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

        //straight up a row
        i = 1;
        while ((row + i) <= 8) {
            ChessPosition endPosition = new ChessPosition((row + i), (col));
            if (board.getPiece(endPosition) == null) {
                ChessMove move = new ChessMove(startPosition, endPosition, null);
                moves.add(move);
                List<Integer> lOfMoves = new ArrayList<Integer>();
                lOfMoves.add(row + i);
                lOfMoves.add(col);
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
                lOfMoves.add(col);
                movesList.add(lOfMoves);
                break;
            }
        }
        // down
        i = 1;
        while ((row - i) > 0) {
            ChessPosition endPosition = new ChessPosition((row - i), (col));
            if (board.getPiece(endPosition) == null) {
                ChessMove move = new ChessMove(startPosition, endPosition, null);
                moves.add(move);
                List<Integer> lOfMoves = new ArrayList<Integer>();
                lOfMoves.add(row - i);
                lOfMoves.add(col);
                movesList.add(lOfMoves);
                ++i;
            } else if(board.getPiece(endPosition) != null && board.getPiece(endPosition).getTeamColor() == tColor)
            {
                break;
            } else if(board.getPiece(endPosition) != null && board.getPiece(endPosition).getTeamColor() != tColor){
                ChessMove move = new ChessMove(startPosition, endPosition, null);
                moves.add(move);
                List<Integer> lOfMoves = new ArrayList<Integer>();
                lOfMoves.add(row - i);
                lOfMoves.add(col);
                movesList.add(lOfMoves);
                break;
            }
        }

        // moving right
        i = 1;
        while ((col + i) < 9) {
            ChessPosition endPosition = new ChessPosition((row), (col + i));
            if (board.getPiece(endPosition) == null) {
                ChessMove move = new ChessMove(startPosition, endPosition, null);
                moves.add(move);
                List<Integer> lOfMoves = new ArrayList<Integer>();
                lOfMoves.add(row);
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
                lOfMoves.add(row);
                lOfMoves.add(col + i);
                movesList.add(lOfMoves);
                break;
            }
        }
        i = 1;
        while ((col - i) > 0) {
            ChessPosition endPosition = new ChessPosition((row), (col - i));
            if (board.getPiece(endPosition) == null) {
                ChessMove move = new ChessMove(startPosition, endPosition, null);
                moves.add(move);
                List<Integer> lOfMoves = new ArrayList<Integer>();
                lOfMoves.add(row);
                lOfMoves.add(col - i);
                movesList.add(lOfMoves);
                ++i;
            } else if(board.getPiece(endPosition) != null && board.getPiece(endPosition).getTeamColor() == tColor)
            {
                break;
            } else if(board.getPiece(endPosition) != null && board.getPiece(endPosition).getTeamColor() != tColor){
                ChessMove move = new ChessMove(startPosition, endPosition, null);
                moves.add(move);
                List<Integer> lOfMoves = new ArrayList<Integer>();
                lOfMoves.add(row);
                lOfMoves.add(col - i);
                movesList.add(lOfMoves);
                break;
            }
        }
        // moving down and to the left
        i = 1;
        while ((row - i) > 0 && (col + i) <= 8) {
            ChessPosition endPosition = new ChessPosition((row - i), (col + i));
            if (board.getPiece(endPosition) == null) {
                ChessMove move = new ChessMove(startPosition, endPosition, null);
                moves.add(move);
                List<Integer> lOfMoves = new ArrayList<Integer>();
                lOfMoves.add(row - i);
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

        //moving down to the left
        i = 1;
        while ((row - i) > 0 && (col - i) > 0) {
            ChessPosition endPosition = new ChessPosition((row - i), (col - i));
            // validateMoves(startPosition, endPosition, moves)
            if (board.getPiece(endPosition) == null) {
                ChessMove move = new ChessMove(startPosition, endPosition, null);
                moves.add(move);
                List<Integer> lOfMoves = new ArrayList<Integer>();
                lOfMoves.add(row - i);
                lOfMoves.add(col - i);
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

        // moving up to the left
        i = 1;
        while ((row + i) <= 8 && (col - i) > 0) {
            ChessPosition endPosition = new ChessPosition((row + i), (col - i));
            if (board.getPiece(endPosition) == null) {
                ChessMove move = new ChessMove(startPosition, endPosition, null);
                moves.add(move);
                List<Integer> lOfMoves = new ArrayList<Integer>();
                lOfMoves.add(row + i);
                lOfMoves.add(col - i);
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


        return moves;
    }
}
