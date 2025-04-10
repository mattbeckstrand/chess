package ui;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static ui.EscapeSequences.*;

public class DrawingChessBoard {

    public static void drawChessBoard(PrintStream out, ChessGame game, String perspectiveColor, List<ChessPosition> highlights) {
        String light = SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_WHITE;
        String dark = SET_BG_COLOR_BLACK + SET_TEXT_COLOR_WHITE;
        String highlightBg = SET_BG_COLOR_GREEN + SET_TEXT_COLOR_BLACK;
        String reset = RESET_BG_COLOR + RESET_TEXT_COLOR;

        String[][] board = new String[10][10];
        boolean isWhitePerspective = Objects.equals(perspectiveColor, "WHITE");

        for (int r = 0; r < 10; r++) {
            for (int c = 0; c < 10; c++) {
                board[r][c] = "   ";
            }
        }

        // Place pieces from the actual game board
        var boardState = game.getBoard();
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition pos = new ChessPosition(row, col);
                var piece = boardState.getPiece(pos);
                if (piece != null) {
                    int boardRow = isWhitePerspective ? row : 9 - row;
                    int boardCol = isWhitePerspective ? col : 9 - col;
                    board[boardRow][boardCol] = piece.getPieceType().toString().substring(0, 1); // You can replace this with actual UI icons
                    if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                        board[boardRow][boardCol] = WHITE_PIECE_MAP.get(piece.getPieceType());
                    } else {
                        board[boardRow][boardCol] = BLACK_PIECE_MAP.get(piece.getPieceType());
                    }
                }
            }
        }

        // Add row/col labels
        for (int r = 1; r <= 8; r++) {
            int labelRow = isWhitePerspective ? 9 - r : r;
            board[r][0] = " " + labelRow + " ";
            board[r][9] = " " + labelRow + " ";
        }

        for (int c = 1; c <= 8; c++) {
            char columnChar = (char) ('a' + (isWhitePerspective ? c - 1 : 8 - c));
            board[0][c] = " " + columnChar + " ";
            board[9][c] = " " + columnChar + " ";
        }

        // Draw the board with highlights
        for (int r = 0; r < 10; r++) {
            for (int c = 0; c < 10; c++) {
                boolean isEdge = r == 0 || r == 9 || c == 0 || c == 9;
                if (isEdge) {
                    out.print(SET_TEXT_COLOR_MAGENTA + board[r][c] + reset);
                } else {
                    int gameRow = isWhitePerspective ? r : 9 - r;
                    int gameCol = isWhitePerspective ? c : 9 - c;
                    ChessPosition currentPos = new ChessPosition(gameRow, gameCol);
                    boolean shouldHighlight = highlights != null && highlights.stream().anyMatch(p -> p.equals(currentPos));
                    String bg = shouldHighlight ? highlightBg : ((r + c) % 2 == 0 ? light : dark);
                    out.print(bg + board[r][c] + reset);
                }
            }
            out.println();
        }
    }
    public static final Map<ChessPiece.PieceType, String> WHITE_PIECE_MAP = Map.of(
            ChessPiece.PieceType.KING, WHITE_KING,
            ChessPiece.PieceType.QUEEN, WHITE_QUEEN,
            ChessPiece.PieceType.ROOK, WHITE_ROOK,
            ChessPiece.PieceType.BISHOP, WHITE_BISHOP,
            ChessPiece.PieceType.KNIGHT, WHITE_KNIGHT,
            ChessPiece.PieceType.PAWN, WHITE_PAWN
    );

    public static final Map<ChessPiece.PieceType, String> BLACK_PIECE_MAP = Map.of(
            ChessPiece.PieceType.KING, BLACK_KING,
            ChessPiece.PieceType.QUEEN, BLACK_QUEEN,
            ChessPiece.PieceType.ROOK, BLACK_ROOK,
            ChessPiece.PieceType.BISHOP, BLACK_BISHOP,
            ChessPiece.PieceType.KNIGHT, BLACK_KNIGHT,
            ChessPiece.PieceType.PAWN, BLACK_PAWN
    );
}
