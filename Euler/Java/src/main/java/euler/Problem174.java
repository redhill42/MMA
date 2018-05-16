package euler;

import static euler.algo.Library.isqrt;

public final class Problem174 {
    private Problem174() {}

    private static class Solver {
        private final int[] table;
        private final int[] count;

        public Solver(int limit) {
            this.table = new int[limit + 1];
            this.count = new int[limit + 1];

            int n = limit / 4;
            for (int a = 1, m = isqrt(n); a < m; a++) {
                for (int b = 1; ; b++) {
                    int k = 4 * a * (a + b);
                    if (k > limit)
                        break;
                    ++table[k];
                }
            }

            int s = 0;
            for (int i = 0; i <= limit; i++) {
                if (table[i] >= 1 && table[i] <= 10)
                    s++;
                count[i] = s;
            }
        }

        public int solve(int k) {
            return count[k];
        }
    }

    public static int solve(int limit) {
        return new Solver(limit).solve(limit);
    }

    public static void main(String[] args) {
        int limit = 1_000_000;
        if (args.length > 0)
            limit = Integer.parseInt(args[0]);
        System.out.println(solve(limit));
    }
}
