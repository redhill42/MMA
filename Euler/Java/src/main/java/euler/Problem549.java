package euler;

import euler.algo.PrimeSieve;

import static euler.algo.Library.isqrt;
import static java.lang.Math.ceil;
import static java.lang.Math.log;
import static java.lang.Math.max;

public final class Problem549 {
    private Problem549() {}

    private static class Solver {
        private final int limit;
        private final PrimeSieve sieve;
        private final int[][] mu;

        Solver(int limit) {
            this.limit = limit;
            this.sieve = new PrimeSieve(limit);
            this.mu = new int[isqrt(limit) + 1][];
        }

        public long solve() {
            int p, a;
            long n;

            for (p = 2; p < mu.length; p = sieve.nextPrime(p)) {
                mu[p] = new int[(int)ceil(log(limit) / log(p))];
                for (a = 1, n = p; n <= limit; a++, n *= p) {
                    mu[p][a] = kempner(p, a);
                }
            }

            return search(2, 1, 0);
        }

        private long search(int p, long n, int mu) {
            long sum = 0;
            for (; p > 0; p = sieve.nextPrime(p)) {
                long next = p * n;
                if (next > limit)
                    break;
                for (int a = 1; next <= limit; a++, next *= p) {
                    int nextMu = max(mu(p, a), mu);
                    sum += nextMu;
                    sum += search(sieve.nextPrime(p), next, nextMu);
                }
            }
            return sum;
        }

        private int mu(int p, int a) {
            return (p < mu.length) ? mu[p][a] : p;
        }

        private static int kempner(int p, int a) {
            if (a <= p)
                return p * a;

            int an = 1, bn = p + 1;
            while (bn <= a) {
                an = bn;
                bn = an * p + 1;
            }

            int k = a / an;
            int r = a % an;
            int s = a * (p - 1) + k;
            while (r != 0) {
                an = (an - 1) / p;
                k = r / an;
                r = r % an;
                s += k;
            }
            return s;
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
