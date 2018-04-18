package euler;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

import static euler.util.Utils.gcd;
import static euler.util.Utils.isqrt;
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
    private static class SolveTask extends RecursiveTask<Long> {
        private final long from, to, limit;

        SolveTask(long limit) {
            this.from = 1;
            this.to = isqrt(limit);
            this.limit = limit;
        }

        SolveTask(long from, long to, long limit) {
            this.from = from;
            this.to = to;
            this.limit = limit;
        }

        @Override
        public Long compute() {
            if (to - from <= 1000) {
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
            } else {
                long middle = (from + to) / 2;
                SolveTask left = new SolveTask(from, middle, limit);
                SolveTask right = new SolveTask(middle + 1, to, limit);
                left.fork();
                right.fork();
                return left.join() + right.join();
            }
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
