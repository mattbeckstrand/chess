package ui;

import chess.ChessBoard;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class DrawingChessBoard {


    public static void drawChessBoard(PrintStream out) {
        String light = EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.SET_TEXT_COLOR_WHITE;
        String dark = EscapeSequences.SET_BG_COLOR_DARK_GREY + EscapeSequences.SET_TEXT_COLOR_WHITE;
        String reset = EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR;

        for (int row = 0; row < 8; row++) {
            for (int line = 0; line < 3; line++) {
                for (int col = 0; col < 8; col++) {
                    boolean isLight = (row + col) % 2 == 0;
                    out.print(isLight ? light : dark);

                    if (line == 1) {
                        out.print(EscapeSequences.EMPTY);
                    } else {
                        out.print("     ");
                    }

                    out.print(reset);
                }
                out.println();
            }
        }
    }
}
