package euler;

import java.util.concurrent.ForkJoinPool;
import euler.util.RangedTask;

import static euler.algo.Library.gcd;
import static euler.algo.Library.isqrt;
import static java.lang.Math.min;

public final class Problem153 {
    private Problem153() {}

    private static long T(long n) {
        return n * (n + 1) / 2;
    }

    private static long sigma(long n) {
        long k, m, s = 0;

        m = isqrt(n);
        for (k = 1; k <= m; k++)
            s += k * (n / k);
        for (k = 1; k <= m; k++)
            s += k * (T(n / k) - T(n / (k + 1)));
        if (n / m == m)
            s -= m * m;
        return s;
    }

    @SuppressWarnings("serial")
    private static class SolveTask extends RangedTask<Long> {
        private final long limit;

        SolveTask(long limit) {
            this(1, (int)isqrt(limit), limit);
        }

        SolveTask(int from, int to, long limit) {
            super(from, to, 1000);
            this.limit = limit;
        }

        @Override
        public Long compute(int from, int to) {
            long sum = 0;
            for (long a = from; a <= to; a++) {
                long m = min(a, isqrt(limit - a * a));
                for (long b = 1; b <= m; b++) {
                    if (gcd(a, b) == 1) {
                        sum += 2 * (a == b ? a : a + b) * sigma(limit / (a * a + b * b));
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

    public static long solve(int limit) {
        ForkJoinPool pool = new ForkJoinPool();
        long result = sigma(limit) + pool.invoke(new SolveTask(limit));
        pool.shutdown();
        return result;
    }

    public static void main(String[] args) {
        System.out.println(solve(100000000));
    }
}
