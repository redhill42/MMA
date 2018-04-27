package euler;

import java.util.concurrent.ForkJoinPool;
import euler.util.RangedTask;

import static euler.util.Utils.gcd;
import static euler.util.Utils.isSquare;

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

        public int compute(int from, int to) {
            int ret = 0;
            for (int a = from; a <= to; a++)
            for (int b = 1; b <= limit; b++)
            for (int c = 1; c <= limit; c++)
            for (int d = 1; d <= limit; d++)
                if (isSquare((pick[a][b] + pick[b][c] + pick[c][d] + pick[d][a]) / 2 + 1))
                    ret++;
            return ret;
        }

        @SuppressWarnings("serial")
        class SolveTask extends RangedTask<Integer> {
            SolveTask(int from, int to) {
                super(from, to, 10);
            }

            @Override
            protected Integer compute(int from, int to) {
                return Solver.this.compute(from, to);
            }

            @Override
            protected Integer combine(Integer v1, Integer v2) {
                return v1 + v2;
            }

            @Override
            protected SolveTask fork(int from, int to) {
                return new SolveTask(from, to);
            }
        }

        public int solve() {
            ForkJoinPool pool = new ForkJoinPool();
            int result = pool.invoke(new SolveTask(1, limit));
            pool.shutdown();
            return result;
        }
    }

    public static int solve(int limit) {
        return new Solver(limit).solve();
    }

    public static void main(String[] args) {
        System.out.println(solve(100));
    }
}
