package euler;

import euler.algo.PrimeSieve;
import static java.lang.Math.log;

public final class Problem204 {
    private Problem204() {}

    public static class Solver {
        private final PrimeSieve sieve;
        private final int maxPrime;

        public Solver(int k) {
            this.sieve = new PrimeSieve(k);
            this.maxPrime = sieve.previousPrime(k + 1);
        }

        public int solve(int n) {
            return solve(2, n);
        }

        private int solve(int p, int n) {
            int k = (int)(log(n) / log(p));
            if (p == maxPrime) {
                return k + 1;
            }

            int q = sieve.nextPrime(p);
            int s = 0, d = 1;
            for (int i = 0; i <= k; i++) {
                s += solve(q, n / d);
                d *= p;
            }
            return s;
        }
    }

    public static int solve(int k, int n) {
        return new Solver(k).solve(n);
    }

    public static void main(String[] args) {
        System.out.println(solve(100, 1_000_000_000));
    }
}
