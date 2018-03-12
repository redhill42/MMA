/**
 * Cloudway Platform
 * Copyright (c) 2012-2016 Cloudway Technology, Inc.
 * All rights reserved.
 */

package minesweeper;

public class Cell implements java.io.Serializable {
    private static final long serialVersionUID = 2919451690435406413L;

    public final int row, col;

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public Cell(int[] c) {
        this(c[0], c[1]);
    }

    public boolean equals(Object obj) {
        if (obj instanceof Cell) {
            Cell other = (Cell)obj;
            return row == other.row && col == other.col;
        }
        return super.equals(obj);
    }

    public int hashCode() {
        return 31*row + col;
    }

    public String toString() {
        return "{"+row+","+col+"}";
    }
}
