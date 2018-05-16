package euler;

import java.util.concurrent.ForkJoinPool;
import euler.util.RangedTask;

import static euler.algo.Library.isqrt;
import static euler.algo.Library.pow;
import static java.lang.Math.log;

public final class Problem346 {
    private Problem346() {}

    @SuppressWarnings("serial")
    private static class SolveTask extends RangedTask<Long> {
        private final long limit;

        SolveTask(long limit) {
            this(2, (int)isqrt(limit), limit);
        }

        SolveTask(int from, int to, long limit) {
            super(from, to);
            this.limit = limit;
        }

        @Override
        public Long compute(int from, int to) {
            long sum = 0;
            for (int b = from; b <= to; b++)
                sum += S(b);
            return sum;
        }

        private long S(long b) {
            int n = (int)(log(limit * (b - 1) + 1) / log(b)) - 1;
            return (n-1) * (b+1) + (pow(b, n) - b * n + n - 1) / (b-1) / (b-1) * b * b;
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
        long result = pool.invoke(new SolveTask(limit)) + 1;
        pool.shutdown();
        if (limit >= 31)
            result -= 31;
        if (limit >= 8191)
            result -= 8191;
        return result;
    }

    public static void main(String[] args) {
        System.out.println(solve(1_000_000_000_000L));
    }
}
