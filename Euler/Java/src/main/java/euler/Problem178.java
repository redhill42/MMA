package euler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public final class Problem178 {
    private Problem178() {}

    private static class Solver {
        private final int maxLen;
        private final Map<Integer, Integer>[] left;
        private final Map<Integer, Integer>[] right;

        private static final int PANDIGITAL = 1023;

        @SuppressWarnings("unchecked")
        Solver(int maxLen) {
            this.maxLen = maxLen;

            int half = (maxLen + 1) / 2;
            left  = new Map[half + 1];
            right = new Map[half + 1];
            Arrays.setAll(left, x -> new HashMap<Integer, Integer>());
            Arrays.setAll(right, x -> new HashMap<Integer, Integer>());
        }

        private void step(int firstDigit, int lastDigit, int mask, int len, int maxLen) {
            mask |= 1 << lastDigit;

            if (len >= 5) {
                int hash;

                if (firstDigit != 0) {
                    hash = (lastDigit << 16) | mask;
                    left[len].merge(hash, 1, Integer::sum);
                }

                hash = (firstDigit << 16) | mask;
                right[len].merge(hash, 1, Integer::sum);
            }

            if (len == maxLen)
                return;
            if (lastDigit != 0)
                step(firstDigit, lastDigit - 1, mask, len + 1, maxLen);
            if (lastDigit != 9)
                step(firstDigit, lastDigit + 1, mask, len + 1, maxLen);
        }

        public long solve() {
            int half = (maxLen + 1) / 2;
            for (int d = 0; d <= 9; d++) {
                step(d, d, 0, 1, half);
            }

            long total = 0;
            for (int len = 10; len <= maxLen; len++)
                total += count(len);
            return total;
        }

        private long count(int len) {
            int leftLen = len / 2;
            int rightLen = len - leftLen;

            long total = 0;
            for (Map.Entry<Integer, Integer> a : left[leftLen].entrySet()) {
                int d1 = a.getKey() >> 16;
                int m1 = a.getKey() & 0xFFFF;
                int c1 = a.getValue();

                for (Map.Entry<Integer, Integer> b : right[rightLen].entrySet()) {
                    int d2 = b.getKey() >> 16;
                    int m2 = b.getKey() & 0xFFFF;
                    int c2 = b.getValue();

                    if (d1 != 0 && d2 == d1 - 1 && (m1 | m2) == PANDIGITAL)
                        total += c1 * c2;
                    if (d1 != 9 && d2 == d1 + 1 && (m1 | m2) == PANDIGITAL)
                        total += c1 * c2;
                }
            }
            return total;
        }
    }

    public static long solve(int maxLen) {
        return new Solver(maxLen).solve();
    }

    public static void main(String[] args) {
        int n = 40;
        if (args.length > 0)
            n = Integer.parseInt(args[0]);
        System.out.println(solve(n));
    }
}
