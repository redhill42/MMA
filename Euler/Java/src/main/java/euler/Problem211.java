package euler;

import java.util.concurrent.ForkJoinPool;
import euler.util.FactorizationSieve;
import euler.util.RangedTask;

import static euler.util.Utils.isSquare;

public final class Problem211 {
    private Problem211() {}

    @SuppressWarnings("serial")
    private static class SolveTask extends RangedTask<Long> {
        private final FactorizationSieve sieve;

        SolveTask(FactorizationSieve sieve, int from, int to) {
            super(from, to, 10000);
            this.sieve = sieve;
        }

        @Override
        public Long compute(int from, int to) {
            long sum = 0;
            for (int n = from; n <= to; n++)
                if (isSquare(sieve.sigma(2, n)))
                    sum += n;
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

    public static long solve(int from, int to) {
        ForkJoinPool pool = new ForkJoinPool();
        FactorizationSieve sieve = new FactorizationSieve(to);
        long result = pool.invoke(new SolveTask(sieve, from, to-1));
        pool.shutdown();
        return result;
    }

    public static void main(String[] args) {
        System.out.println(solve(1, 64_000_000));
    }
}
