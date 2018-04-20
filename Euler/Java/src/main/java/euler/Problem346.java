package euler;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

import static euler.util.Utils.isqrt;
import static euler.util.Utils.pow;
import static java.lang.Math.log;

public final class Problem346 {
    private Problem346() {}

    @SuppressWarnings("serial")
    private static class SolveTask extends RecursiveTask<Long> {
        private final int from, to;
        private final long limit;

        SolveTask(long limit) {
            this.from = 2;
            this.to = (int)isqrt(limit);
            this.limit = limit;
        }

        SolveTask(int from, int to, long limit) {
            this.from = from;
            this.to = to;
            this.limit = limit;
        }

        @Override
        public Long compute() {
            if (to - from <= 100) {
                long sum = 0;
                for (int b = from; b <= to; b++)
                    sum += S(b);
                return sum;
            } else {
                int middle = (from + to) / 2;
                SolveTask left = new SolveTask(from, middle, limit);
                SolveTask right = new SolveTask(middle + 1, to, limit);
                left.fork();
                right.fork();
                return left.join() + right.join();
            }
        }

        private long S(long b) {
            int n = (int)(log(limit * (b - 1) + 1) / log(b)) - 1;
            return (n-1) * (b+1) + (pow(b, n) - b * n + n - 1) / (b-1) / (b-1) * b * b;
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
