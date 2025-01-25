package chess;

import java.util.*;

import chess.ChessPosition;
import chess.ChessPiece;

public class PawnMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        //need start position, end position and promotion piece
        int startRow = position.getRow();
        int startCol = position.getColumn();
        ChessPosition startPosition = new ChessPosition(startRow, startCol);
        //get piece and check the team color, used to validate moves
        ChessPiece startingPiece = board.getPiece(startPosition);
        ChessGame.TeamColor tColor = startingPiece.getTeamColor();
        //start a list to put all of the chess moves to be returned
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        //list to print out to check the integers of the moves I return
        List<List<Integer>> movesList = new ArrayList<>();


        int i = 1;
        int sRow;
        ArrayList<ChessPosition> positionCheck = new ArrayList<ChessPosition>();
        ChessPosition check1;
        ChessPosition check2;
        ChessPosition check3;
        ChessPosition check4;

        if (tColor == ChessGame.TeamColor.BLACK) {
            check1 = new ChessPosition(startRow - 1, startCol);
            check2 = new ChessPosition(startRow - 2, startCol);
            check3 = new ChessPosition(startRow - 1, startCol - 1);
            check4 = new ChessPosition(startRow - 1, startCol + 1);
        } else {
            check1 = new ChessPosition(startRow + 1, startCol);
            check2 = new ChessPosition(startRow + 2, startCol);
            check3 = new ChessPosition(startRow + 1, startCol - 1);
            check4 = new ChessPosition(startRow + 1, startCol + 1);
        }
        if (tColor == ChessGame.TeamColor.BLACK) {

            //check move 1, if its null and its not equal to 1, add
            if (board.getPiece(check1) == null && startRow - 1 > 1) {
                ChessMove move1 = new ChessMove(startPosition, check1, null);
                System.out.println("move1 added");
                moves.add(move1);
            } else if (board.getPiece(check1) == null && startRow - 1 == 1) {
                moves.add(new ChessMove(startPosition, check1, ChessPiece.PieceType.QUEEN));
                moves.add(new ChessMove(startPosition, check1, ChessPiece.PieceType.ROOK));
                moves.add(new ChessMove(startPosition, check1, ChessPiece.PieceType.KNIGHT));
                moves.add(new ChessMove(startPosition, check1, ChessPiece.PieceType.BISHOP));
            }
            //move 2
            if (startRow - 2 > 1 && board.getPiece(check2) == null && board.getPiece(check1) == null && startRow == 7) {
                System.out.println("move2 added");
                ChessMove move2 = new ChessMove(startPosition, check2, null);
                moves.add(move2);
            }
            //diagnol to the lef
            if (startCol - 1 > 0 && board.getPiece(check3) != null && board.getPiece(check3).getTeamColor() != ChessGame.TeamColor.BLACK) {
                if (startRow - 1 > 1) {
                    ChessMove move3 = new ChessMove(startPosition, check3, null);
                    moves.add(move3);
                    System.out.println("move 3 no prom");
                } else if (startRow - 1 == 1) {
                    System.out.println("move3 w prom");
                    moves.add(new ChessMove(startPosition, check3, ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(startPosition, check3, ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(startPosition, check3, ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(startPosition, check3, ChessPiece.PieceType.BISHOP));
                }
            }

            if (startCol + 1 <= 8 && board.getPiece(check4) != null && board.getPiece(check4).getTeamColor() != ChessGame.TeamColor.BLACK) {
                System.out.println("were in");
                if (startRow - 1 > 1) {
                    ChessMove move4 = new ChessMove(startPosition, check4, null);
                    moves.add(move4);
                    System.out.println("move4 no prom");
                } else if (startRow - 1 == 1) {
                    System.out.println("move4 w prom");
                    moves.add(new ChessMove(startPosition, check4, ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(startPosition, check4, ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(startPosition, check4, ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(startPosition, check4, ChessPiece.PieceType.BISHOP));
                }
            }
        }
            if (tColor == ChessGame.TeamColor.WHITE) {

                //check move 1, if its null and its not equal to 1, add
                if (board.getPiece(check1) == null && startRow + 1 < 8) {
                    ChessMove move1 = new ChessMove(startPosition, check1, null);
                    System.out.println("move1 added");
                    moves.add(move1);
                } else if (board.getPiece(check1) == null && startRow + 1 == 8) {
                    moves.add(new ChessMove(startPosition, check1, ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(startPosition, check1, ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(startPosition, check1, ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(startPosition, check1, ChessPiece.PieceType.BISHOP));
                }
                //move 2
                if (startRow + 2 < 8 && board.getPiece(check2) == null && startRow == 2) {
                    System.out.println("move2 added");
                    ChessMove move2 = new ChessMove(startPosition, check2, null);
                    moves.add(move2);
                }
                //diagnol to the left
                if (startCol - 1 > 0 && board.getPiece(check3) != null && board.getPiece(check3).getTeamColor() != ChessGame.TeamColor.WHITE) {
                    System.out.println("were in");
                    if (startRow + 1 < 8 ) {
                        ChessMove move3 = new ChessMove(startPosition, check3, null);
                        moves.add(move3);
                        System.out.println("move 3 no prom");
                    } else if (startRow + 1 == 8) {
                        System.out.println("move3 w prom");
                        moves.add(new ChessMove(startPosition, check3, ChessPiece.PieceType.QUEEN));
                        moves.add(new ChessMove(startPosition, check3, ChessPiece.PieceType.ROOK));
                        moves.add(new ChessMove(startPosition, check3, ChessPiece.PieceType.KNIGHT));
                        moves.add(new ChessMove(startPosition, check3, ChessPiece.PieceType.BISHOP));
                    }
                }

                if (startCol + 1 <= 8 && board.getPiece(check4) != null && board.getPiece(check4).getTeamColor() != ChessGame.TeamColor.WHITE) {
                    if (startRow + 1 < 8) {
                        ChessMove move4 = new ChessMove(startPosition, check4, null);
                        moves.add(move4);
                        System.out.println("move4 no prom");
                    } else if (startRow + 1 == 8) {
                        System.out.println("move4 w prom");
                        moves.add(new ChessMove(startPosition, check4, ChessPiece.PieceType.QUEEN));
                        moves.add(new ChessMove(startPosition, check4, ChessPiece.PieceType.ROOK));
                        moves.add(new ChessMove(startPosition, check4, ChessPiece.PieceType.KNIGHT));
                        moves.add(new ChessMove(startPosition, check4, ChessPiece.PieceType.BISHOP));
                    }
                }

            }

        return moves;
    }
}

