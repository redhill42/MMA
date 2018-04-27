package euler;

import java.util.concurrent.ForkJoinPool;
import euler.util.RangedTask;

import static euler.util.Utils.gcd;
import static euler.util.Utils.isSquare;

public final class Problem141 {
    private Problem141() {}

    @SuppressWarnings("serial")
    private static class SolveTask extends RangedTask<Long> {
        private final long limit;

        SolveTask(long limit) {
            this(2, (int)Math.pow(limit, 1.0/3), limit);
        }

        SolveTask(int from, int to, long limit) {
            super(from, to);
            this.limit = limit;
        }

        @Override
        protected Long compute(int from, int to) {
            long sum = 0;
            for (long a = from; a <= to; a++) {
                for (long b = 1; b < a; b++) {
                    if (gcd(a, b) != 1)
                        continue;
                    for (long c = 1; c < limit; c++) {
                        long n = a*a*a*b*c*c + b*b*c;
                        if (n > limit)
                            break;
                        if (isSquare(n))
                            sum += n;
                    }
                }
            }
            return sum;
        }

        @Override
        protected Long combine(Long v1, Long v2) {
            return v1 + v2;
        }

        @Override
        protected SolveTask fork(int from, int to) {
            return new SolveTask(from, to, limit);
        }
    }

    public static long solve(long limit) {
        ForkJoinPool pool = new ForkJoinPool();
        long result = pool.invoke(new SolveTask(limit));
        pool.shutdown();
        return result;
    }

    public static void main(String[] args) {
        System.out.println(solve(1_000_000_000_000L));
    }
}
