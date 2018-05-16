package euler;

import java.util.concurrent.ForkJoinPool;
import euler.util.RangedTask;
import euler.algo.TotientSieve;
import static euler.algo.Library.chineseRemainder;

public final class Problem531 {
    private Problem531() {}

    @SuppressWarnings("serial")
    private static class SolveTask extends RangedTask<Long> {
        private final TotientSieve sieve;
        private final int end;

        SolveTask(TotientSieve sieve, int from, int to, int end) {
            super(from, to);
            this.sieve = sieve;
            this.end = end;
        }

        @Override
        public Long compute(int from, int to) {
            long sum = 0;
            for (int n = from; n <= to; n++) {
                for (int m = n + 1; m < end; m++)
                    sum += chineseRemainder(sieve.phi(n), n, sieve.phi(m), m);
            }
            return sum;
        }

        @Override
        protected Long combine(Long v1, Long v2) {
            return v1 + v2;
        }

        @Override
        protected SolveTask fork(int from, int to) {
            return new SolveTask(sieve, from, to, end);
        }
    }

    public static long solve(int start, int end) {
        TotientSieve sieve = new TotientSieve(end);
        ForkJoinPool pool = new ForkJoinPool();
        long result = pool.invoke(new SolveTask(sieve, start, end - 1, end));
        pool.shutdown();
        return result;
    }

    public static void main(String[] args) {
        System.out.println(solve(1_000_000, 1_005_000));
    }
}
