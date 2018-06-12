package euler;

import java.util.concurrent.ForkJoinPool;

import euler.algo.FactorizationSieve;
import euler.algo.PrimeFactor;
import euler.util.RangedTask;

import static euler.algo.Library.lcm;
import static euler.algo.Library.pow;

public final class Problem417 {
    private Problem417() {}

    private static class Solver {
        private final int limit;
        private final FactorizationSieve sieve;
        private final int[] primePeriods;

        Solver(int limit) {
            this.limit = limit;
            this.sieve = new FactorizationSieve(limit);
            this.primePeriods = new int[limit + 1];
            this.primePeriods[3] = 1;
        }

        private long period(int n) {
            while (n % 2 == 0)
                n >>= 1;
            while (n % 5 == 0)
                n /= 5;
            if (n == 1)
                return 0;
            if (sieve.isPrime(n))
                return primePeriods[n];

            long l = 1;
            for (PrimeFactor f : sieve.factors(n)) {
                int p = (int)f.prime(), a = f.power();
                a -= (p == 3 || p == 487) ? 2 : 1;
                if (a > 0) {
                    l = lcm(l, pow(p, a) * primePeriods[p]);
                } else {
                    l = lcm(l, primePeriods[p]);
                }
            }
            return l;
        }

        @SuppressWarnings("serial")
        class SieveTask extends RangedTask<Void> {
            SieveTask(int from, int to) {
                super(from, to);
            }

            @Override
            protected Void compute(int from, int to) {
                for (int p = sieve.nextPrime(from-1); p > 0 && p <= to; p = sieve.nextPrime(p))
                    primePeriods[p] = sieve.ord(10, p);
                return null;
            }

            @Override
            protected Void combine(Void v1, Void v2) {
                return null;
            }

            @Override
            protected SieveTask fork(int from, int to) {
                return new SieveTask(from, to);
            }
        }

        @SuppressWarnings("serial")
        class SolveTask extends RangedTask<Long> {
            SolveTask(int from, int to) {
                super(from, to);
            }

            @Override
            protected Long compute(int from, int to) {
                long sum = 0;
                for (int n = from; n <= to; n++)
                    sum += period(n);
                return sum;
            }

            @Override
            protected Long combine(Long v1, Long v2) {
                return v1 + v2;
            }

            @Override
            protected SolveTask fork(int from, int to) {
                return new SolveTask(from, to);
            }
        }

        public long solve() {
            ForkJoinPool pool = new ForkJoinPool();
            pool.invoke(new SieveTask(7, limit));
            long result = pool.invoke(new SolveTask(3, limit));
            pool.shutdown();
            return result;
        }
    }

    public static long solve(int limit) {
        return new Solver(limit).solve();
    }

    public static void main(String[] args) {
        int n = 100_000_000;
        if (args.length > 0)
            n = Integer.parseInt(args[0]);
        System.out.println(solve(n));
    }
}
