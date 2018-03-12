/**
 * Cloudway Platform
 * Copyright (c) 2012-2016 Cloudway Technology, Inc.
 * All rights reserved.
 */

package minesweeper;

public class Field implements java.io.Serializable {
    private static final long serialVersionUID = -2717546811245932276L;

    public final String[][] m;

    public Field(String[][] m) {
        this.m = m;
    }

    public String toString() {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < m.length; i++) {
            b.append('|');
            for (int j = 0; j < m[i].length; j++) {
                b.append(m[i][j]).append(' ');
            }
            b.append("|\n");
        }
        return b.toString();
    }
}
