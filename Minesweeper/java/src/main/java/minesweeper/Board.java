/**
 * Cloudway Platform
 * Copyright (c) 2012-2016 Cloudway Technology, Inc.
 * All rights reserved.
 */

package minesweeper;

import java.util.List;

public interface Board {
    int rows();
    int cols();
    int mines();

    boolean started();
    boolean stopped();
    boolean boomed();
    boolean success();

    int remaining();
    int minesRemaining();
    double timeUsed();

    void reset(int rows, int cols, int mines);
    void restart(boolean keep);
    void attach(BoardListener listener);
    int[][] show();

    Cell click(int row, int col, boolean cheat);
    Cell randomClick(boolean cheat);
    Cell mark(int row, int col);

    boolean solve(boolean greedy, boolean clickOnly, List<Cell> solved);
}
