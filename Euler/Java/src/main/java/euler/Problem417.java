package euler;

import euler.algo.FactorizationSieve;
import euler.algo.PrimeFactor;
import euler.util.LongRangedTask;

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

        public long solve() {
            LongRangedTask.parallel(7, limit, (from, to) -> {
                int p = sieve.nextPrime(from - 1);
                while (p > 0 && p <= to) {
                    primePeriods[p] = sieve.ord(10, p);
                    p = sieve.nextPrime(p);
                }
                return 0;
            });

            return LongRangedTask.parallel(3, limit, (from, to) -> {
                long sum = 0;
                for (int n = from; n <= to; n++)
                    sum += period(n);
                return sum;
            });
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
