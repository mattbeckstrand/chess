package ui;

import chess.ChessGame;
import chess.ChessPosition;

import java.io.PrintStream;
import java.util.Objects;

import static ui.EscapeSequences.*;

public class DrawingChessBoard {

    public static void drawChessBoard(PrintStream out, String color) {
        String light = SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_WHITE;
        String dark = SET_BG_COLOR_BLACK + SET_TEXT_COLOR_WHITE;
        String reset = RESET_BG_COLOR + RESET_TEXT_COLOR;

        String[][] board = new String[10][10];
        boolean isWhitePerspective = (Objects.equals(color, "WHITE"));

        for (int r = 0; r < 10; r++) {
            for (int c = 0; c < 10; c++) {
                board[r][c] = "   ";
            }
        }

        for (int r = 1; r <= 8; r++) {
            for (int c = 1; c <= 8; c++) {
                int boardRow = isWhitePerspective ? r : 9 - r;
                int boardCol = isWhitePerspective ? c : 9 - c;

                if (boardRow == 2) {
                    board[r][c] = WHITE_PAWN;
                }
                if (boardRow == 7) {
                    board[r][c] = BLACK_PAWN;
                }
                if (boardRow == 1) {
                        if(boardCol == 1 || boardCol == 8) {
                             board[r][c] = WHITE_ROOK;
                        }
                        if(boardCol == 2 || boardCol == 7) {
                            board[r][c] = WHITE_KNIGHT;
                        }
                        if(boardCol == 3 || boardCol == 6) {
                            board[r][c] = WHITE_BISHOP;
                        }
                        if(boardCol == 4 ) {
                            board[r][c] = WHITE_QUEEN;
                        }
                        if(boardCol == 5 ) {
                            board[r][c] = WHITE_KING;
                        }
                    }

                if (boardRow == 8) {
                    if(boardCol == 1 || boardCol == 8) {
                        board[r][c] = BLACK_ROOK;
                    }
                    if(boardCol == 2 || boardCol == 7) {
                        board[r][c] = BLACK_KNIGHT;
                    }
                    if(boardCol == 3 || boardCol == 6) {
                        board[r][c] = BLACK_BISHOP;
                    }
                    if(boardCol == 4 ) {
                        board[r][c] = BLACK_QUEEN;
                    }
                    if(boardCol == 5) {
                        board[r][c] = BLACK_KING;
                    }
                }
                }
            }


        for (int r = 1; r <= 8; r++) {
            int labelRow = isWhitePerspective ? 9 - r : r;
            board[r][0] = " " + labelRow + " ";
            board[r][9] = " " + labelRow + " ";
        }

        for (int c = 1; c <= 8; c++) {
            char file = (char) ('a' + (isWhitePerspective ? c - 1 : 8 - c));
            board[0][c] = " " + file + " ";
            board[9][c] = " " + file + " ";
        }

        for (int r = 0; r < 10; r++) {
            for (int c = 0; c < 10; c++) {
                if (r == 0 || r == 9 || c == 0 || c == 9) {
                    out.print(SET_TEXT_COLOR_MAGENTA + board[r][c] + reset);
                } else {
                    boolean isLight = (r + c) % 2 == 0;
                    String bg = isLight ? light : dark;
                    out.print(bg + board[r][c] + reset);
                }
            }
            out.println();
        }
    }
}




