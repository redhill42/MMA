package euler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import euler.util.IntArray;

public final class Problem215 {
    private Problem215() {}

    private static class Solver {
        private IntArray[] crackFree;
        private final Map<Long, Long> memo = new HashMap<>();

        public long solve(int width, int height) {
            List<IntArray> rows = new ArrayList<>();
            makeRows(0, width, new IntArray(width/2), rows);

            int validRows = rows.size();
            crackFree = new IntArray[validRows];
            Arrays.setAll(crackFree, x -> new IntArray());
            for (int i = 0; i < validRows; i++) {
                for (int j = i+1; j < validRows; j++) {
                    if (crackFree(rows.get(i), rows.get(j))) {
                        crackFree[i].add(j);
                        crackFree[j].add(i);
                    }
                }
            }

            long sum = 0;
            for (int row = 0; row < validRows; row++)
                sum += search(row, height);
            return sum;
        }

        private long search(int row, int height) {
            if (height == 1)
                return 1;

            long hash = ((long)height << 32) | row;
            return memo.computeIfAbsent(hash, x -> {
                long sum = 0;
                for (int next : crackFree[row])
                    sum += search(next, height - 1);
                return sum;
            });
        }

        private static void makeRows(int len, int maxlen, IntArray row, List<IntArray> rows) {
            if (len >= maxlen) {
                if (len == maxlen)
                    rows.add(row);
                return;
            }

            if (len != 0) row.add(len);
            makeRows(len + 2, maxlen, row.clone(), rows);
            makeRows(len + 3, maxlen, row, rows);
        }

        private static boolean crackFree(IntArray row1, IntArray row2) {
            for (int i = 0, j = 0; i < row1.length && j < row2.length; ) {
                if (row1.a[i] < row2.a[j]) {
                    i++;
                } else if (row1.a[i] > row2.a[j]) {
                    j++;
                } else {
                    return false;
                }
            }
            return true;
        }
    }

    public static long solve(int width, int height) {
        return new Solver().solve(width, height);
    }

    public static void main(String[] args) {
        System.out.println(solve(32, 10));
    }
}
