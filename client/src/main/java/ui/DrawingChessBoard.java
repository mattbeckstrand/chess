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

    public static void drawChessBoard(PrintStream out, ChessGame game, ChessGame.TeamColor perspective, List<ChessPosition> highlights) {
        String light = SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK;
        String dark = SET_BG_COLOR_BLACK + SET_TEXT_COLOR_WHITE;
        String highlightBg = SET_BG_COLOR_GREEN + SET_TEXT_COLOR_BLACK;
        String reset = RESET_BG_COLOR + RESET_TEXT_COLOR;

        boolean isWhitePerspective = (perspective == ChessGame.TeamColor.WHITE);

        int[] rowOrder = isWhitePerspective ? new int[]{8, 7, 6, 5, 4, 3, 2, 1} : new int[]{1, 2, 3, 4, 5, 6, 7, 8};
        int[] colOrder = isWhitePerspective ? new int[]{1, 2, 3, 4, 5, 6, 7, 8} : new int[]{8, 7, 6, 5, 4, 3, 2, 1};

        out.print("   ");
        for (int col : colOrder) {
            char colLabel = (char) ('a' + col - 1);
            out.print(SET_TEXT_COLOR_MAGENTA + " " + colLabel + " " + reset);
        }
        out.println();

        for (int row : rowOrder) {
            out.print(SET_TEXT_COLOR_MAGENTA + " " + row + " " + reset);
            for (int col : colOrder) {
                ChessPiece piece = game.getBoard().getPiece(new ChessPosition(row, col));
                String square = "   ";
                if (piece != null) {
                    square = piece.getTeamColor() == ChessGame.TeamColor.WHITE
                            ? WHITE_PIECE_MAP.get(piece.getPieceType())
                            : BLACK_PIECE_MAP.get(piece.getPieceType());
                }

                boolean shouldHighlight = highlights != null && highlights.contains(new ChessPosition(row, col));
                String background = ((row + col) % 2 == 0) ? light : dark;
                if (shouldHighlight) background = highlightBg;

                out.print(background + square + reset);
            }
            out.print(SET_TEXT_COLOR_MAGENTA + " " + row + " " + reset);
            out.println();
        }

        out.print("   ");
        for (int col : colOrder) {
            char colLabel = (char) ('a' + col - 1);
            out.print(SET_TEXT_COLOR_MAGENTA + " " + colLabel + " " + reset);
        }
        out.println();
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
