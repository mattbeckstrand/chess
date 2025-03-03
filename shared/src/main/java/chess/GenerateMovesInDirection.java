package chess;

import java.util.ArrayList;
import java.util.Collection;

public class GenerateMovesInDirection {
    private final ChessBoard board;
    private final ChessPosition startPosition;
    private ChessPiece startingPiece;
    private ChessGame.TeamColor tColor;
    private ArrayList<ChessMove> moves;
    private final int[][] directions;
    private int dirRow;
    private int dirCol;

    public GenerateMovesInDirection(ChessBoard board, ChessPosition startPosition, int[][] directions) {
        this.board = board;
        this.startPosition = startPosition;
        this.directions = directions;
    }
        public Collection<ChessMove> callGenMoves(){
            this.moves = new ArrayList<ChessMove>();
            this.startingPiece =  board.getPiece(startPosition);
            this.tColor = startingPiece.getTeamColor();
            for (int[] dir : directions) {
                this.dirRow = dir[0];
                this.dirCol = dir[1];
                this.generateMoves();
            }
            return moves;
        }

        public void generateMoves() {
            int row = startPosition.getRow();
            int col = startPosition.getColumn();

            int i = 1;
            while (true) {
                int newRow = row + (i * dirRow);
                int newCol = col + (i * dirCol);

                if (newRow < 1 || newRow > 8 || newCol < 1 || newCol > 8) {
                    break;
                }

                ChessPosition endPosition = new ChessPosition(newRow, newCol);
                ChessPiece pieceAtEnd = board.getPiece(endPosition);

                if (pieceAtEnd == null) {
                    moves.add(new ChessMove(startPosition, endPosition, null));
                } else {
                    if (pieceAtEnd.getTeamColor() != tColor) {
                        moves.add(new ChessMove(startPosition, endPosition, null)); // Capture move
                    }
                    break;
                }

                i++;
            }
    }
}

