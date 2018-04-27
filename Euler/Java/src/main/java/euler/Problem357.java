package euler;

import java.util.concurrent.ForkJoinPool;
import euler.util.PrimeSieve;
import euler.util.RangedTask;
import static euler.util.Utils.isqrt;

public final class Problem357 {
    private Problem357() {}

    @SuppressWarnings("serial")
    private static class SolveTask extends RangedTask<Long> {
        private final PrimeSieve sieve;

        SolveTask(PrimeSieve sieve, int from, int to) {
            super(from, to);
            this.sieve = sieve;
        }

        @Override
        public Long compute(int from, int to) {
            long sum = 0;
            for (int i = from; i <= to; i++) {
                if (sieve.isPrime(i) && check(i - 1))
                    sum += i - 1;
            }
            return sum;
        }

        private boolean check(int n) {
            for (int i = 1, sq = isqrt(n); i <= sq; i++) {
                if (n % i == 0 && !sieve.isPrime(i + n / i))
                    return false;
            }
            return true;
        }

        @Override
        protected Long combine(Long v1, Long v2) {
            return v1 + v2;
        }

        @Override
        protected SolveTask fork(int from, int to) {
            return new SolveTask(sieve, from, to);
        }
    }

    public static long solve(int limit) {
        ForkJoinPool pool = new ForkJoinPool();
        PrimeSieve sieve = new PrimeSieve(limit);
        long result = pool.invoke(new SolveTask(sieve, 2, limit));
        pool.shutdown();
        return result;
    }

    public static void main(String[] args) {
        System.out.println(solve(100_000_000));
    }
}