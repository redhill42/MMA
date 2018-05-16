package euler;

import java.util.concurrent.ForkJoinPool;
import euler.algo.PrimeSieve;
import euler.util.RangedTask;

public final class Problem381 {
    private Problem381() {}

    @SuppressWarnings("serial")
    private static class SolveTask extends RangedTask<Long> {
        private final PrimeSieve sieve;

        SolveTask(PrimeSieve sieve, int from, int to) {
            super(from, to, 1000);
            this.sieve = sieve;
        }

        @Override
        public Long compute(int from, int to) {
            long sum = 0;
            int p = sieve.nextPrime(from - 1);
            while (p > 0 && p <= to) {
                sum += ((3 * p % 8) * p - 3) / 8;
                p = sieve.nextPrime(p);
            }
            return sum;
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
        PrimeSieve sieve = new PrimeSieve(limit);
        ForkJoinPool pool = new ForkJoinPool();
        long result = pool.invoke(new SolveTask(sieve, 5, limit-1));
        pool.shutdown();
        return result;
    }

    public static void main(String[] args) {
        int limit = 100_000_000;
        if (args.length > 0)
            limit = Integer.parseInt(args[0]);
        System.out.println(solve(limit));
    }
}
