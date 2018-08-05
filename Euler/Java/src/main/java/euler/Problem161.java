package euler;

import java.util.HashMap;
import java.util.Map;

public final class Problem161 {
    private Problem161() {}

    private static class Solver {
        private final Map<Long, Long> memo = new HashMap<>();
        private final int width, height;

        Solver(int width, int height) {
            this.width = width;
            this.height = height;
        }

        long solve() {
            return search(height, 0, 0, 0);
        }

        private long search(int rowsLeft, int row1, int row2, int row3) {
            if (rowsLeft == 0)
                return 1;

            // does first row full filled?
            if (row1 == (1 << width) - 1)
                return search(rowsLeft - 1, row2, row3, 0);

            // find empty cell in first row
            int cell = 0;
            while ((row1 & (1 << cell)) != 0)
                cell++;

            // create unique ID to lookup from cache
            long hash;
            hash = rowsLeft;
            hash <<= width;
            hash |= row1;
            hash <<= width;
            hash |= row2;
            hash <<= width;
            hash |= row3;

            if (memo.containsKey(hash))
                return memo.get(hash);

            int a, b, c;
            long res = 0;

            if (rowsLeft >= 2 && cell < width - 1 &&
                (a = fill(cell, row1)) != 0 &&           // **
                (a = fill(cell+1, a)) != 0 &&            // *
                (b = fill(cell, row2)) != 0)
                res += search(rowsLeft, a, b, row3);

            if (rowsLeft >= 2 && cell < width - 1 &&
                (a = fill(cell, row1)) != 0 &&           // **
                (a = fill(cell+1, a)) != 0 &&            //  *
                (b = fill(cell+1, row2)) != 0)
                res += search(rowsLeft, a, b, row3);

            if (rowsLeft >= 2 && cell < width - 1 &&
                (a = fill(cell, row1)) != 0 &&           // *
                (b = fill(cell, row2)) != 0 &&           // **
                (b = fill(cell+1, b)) != 0)
                res += search(rowsLeft, a, b, row3);

            if (rowsLeft >= 2 && cell > 0 && cell < width &&
                (a = fill(cell, row1)) != 0 &&           //  *
                (b = fill(cell-1, row2)) != 0 &&         // **
                (b = fill(cell, b)) != 0)
                res += search(rowsLeft, a, b, row3);

            if (rowsLeft >= 3 && cell < width &&
                (a = fill(cell, row1)) != 0 &&           // *
                (b = fill(cell, row2)) != 0 &&           // *
                (c = fill(cell, row3)) != 0)             // *
                res += search(rowsLeft, a, b, c);

            if (rowsLeft >= 1 && cell < width - 2 &&
                (a = fill(cell, row1)) != 0 &&           // ***
                (a = fill(cell+1, a)) != 0 &&
                (a = fill(cell+2, a)) != 0)
                res += search(rowsLeft, a, row2, row3);

            memo.put(hash, res);
            return res;
        }

        private static int fill(int pos, int row) {
            int bit = 1 << pos;
            if ((row & bit) == 0)
                return row | bit;
            return 0;
        }
    }

    public static long solve(int width, int height) {
        if (width > height)
            return solve(height, width);
        return new Solver(width, height).solve();
    }

    public static void main(String[] args) {
        System.out.println(solve(9, 12));
    }
}
