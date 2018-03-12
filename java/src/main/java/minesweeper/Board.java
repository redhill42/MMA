/**
 * Cloudway Platform
 * Copyright (c) 2012-2016 Cloudway Technology, Inc.
 * All rights reserved.
 */

package minesweeper;

public interface Board {
    int rows();
    int cols();
    int mines();

    String started();
    String boomed();
    String success();

    int remaining();
    int minesRemaining();
    double timeUsed();

    void reset(int rows, int cols, int mines);
    void restart(boolean keep);
    void attach(BoardListener listener);
    Field show();

    Cell click(int row, int col, boolean cheat);
    Cell randomClick(boolean cheat);
    Cell mark(int row, int col);

    String solve(boolean greedy, boolean clickOnly);
}
