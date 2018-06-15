package euler;

import java.util.concurrent.ForkJoinPool;
import euler.util.RangedTask;

import static euler.algo.Library.even;
import static euler.algo.Library.isqrt;
import static euler.algo.Library.modmul;

public final class Problem401 {
    private Problem401() {}

    @SuppressWarnings("serial")
    private static class SolveTask extends RangedTask<Long> {
        private final long n, m;

        static SolveTask create(long n, long m) {
            return new SolveTask(1, (int)isqrt(n), n, m);
        }

        private SolveTask(int from, int to, long n, long m) {
            super(from, to);
            this.n = n;
            this.m = m;
        }

        @Override
        protected Long compute(int from, int to) {
            long sqrt_n = isqrt(n);
            long sum = 0;
            for (long a = from; a <= to; a++) {
                long b = n / a;
                sum += P(b, m) - modmul(sqrt_n - b, a * a, m);
            }
            return sum % m;
        }

        private static long P(long n, long m) {
            long a, b, c;

            if (even(n)) {
                a = n / 2;
                b = n + 1;
                c = 2 * n + 1;
            } else {
                a = n;
                b = (n + 1) / 2;
                c = 2 * n + 1;
            }

            if (a % 3 == 0) {
                a /= 3;
            } else if (b % 3 == 0) {
                b /= 3;
            } else {
                c /= 3;
            }

            return modmul(a, modmul(b, c, m), m);
        }

        @Override
        protected Long combine(Long v1, Long v2) {
            return (v1 + v2) % m;
        }

        @Override
        protected RangedTask<Long> fork(int from, int to) {
            return new SolveTask(from, to, n, m);
        }
    }

    public static long solve(long n, long m) {
        ForkJoinPool pool = new ForkJoinPool();
        long result = pool.invoke(SolveTask.create(n, m));
        pool.shutdown();
        return result;
    }

    public static void main(String[] args) {
        long n = (long)1e15;
        if (args.length > 0)
            n = Long.parseLong(args[0]);
        System.out.println(solve(n, (long)1e9));
    }
}
