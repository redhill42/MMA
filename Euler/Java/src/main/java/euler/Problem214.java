package euler;

import euler.algo.TotientSieve;

public final class Problem214 {
    private Problem214() {}

    private static class Solver {
        private final TotientSieve sieve;
        private final int length;

        Solver(int limit, int length) {
            this.sieve = new TotientSieve(limit);
            this.length = length;
        }

        public long solve() {
            long sum = 0;
            for (int p = 2; p > 0; p = sieve.nextPrime(p))
                if (test(p))
                    sum += p;
            return sum;
        }

        private boolean test(int n) {
            int k = 1;
            while (k < length && n > 1) {
                n = sieve.phi(n);
                k++;
            }
            return k == length && n == 1;
        }
    }

    public static long solve(int limit, int length) {
        return new Solver(limit, length).solve();
    }

    public static void main(String[] args) {
        System.out.println(solve(40_000_000, 25));
    }
}
