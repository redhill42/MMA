package euler;

import euler.util.LongRangedTask;

import static euler.algo.Library.gcd;
import static euler.algo.Library.isSquare;

public final class Problem504 {
    private Problem504() {}

    static class Solver {
        private final int limit;
        private final int[][] pick;

        public Solver(int limit) {
            this.limit = limit;
            this.pick = new int[limit + 1][limit + 1];
            for (int a = 1; a <= limit; a++)
                for (int b = 1; b <= limit; b++)
                    pick[a][b] = a * b - gcd(a, b);
        }

        public long solve() {
            return LongRangedTask.parallel(1, limit, 10, (from, to) -> {
                int ret = 0;
                for (int a = from; a <= to; a++)
                for (int b = 1; b <= limit; b++)
                for (int c = 1; c <= limit; c++)
                for (int d = 1; d <= limit; d++)
                    if (isSquare((pick[a][b] + pick[b][c] + pick[c][d] + pick[d][a]) / 2 + 1))
                        ret++;
                return ret;
            });
        }
    }

    public static long solve(int limit) {
        return new Solver(limit).solve();
    }

    public static void main(String[] args) {
        System.out.println(solve(100));
    }
}
