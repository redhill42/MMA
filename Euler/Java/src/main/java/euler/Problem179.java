package euler;

import euler.algo.PrimeSieve;
import static euler.algo.Library.exponent;

public final class Problem179 {
    private Problem179() {}

    private static class Solver {
        private final int[] accum;

        public Solver(int limit) {
            PrimeSieve sieve = new PrimeSieve(limit);
            int[] accum = new int[limit + 1];
            for (int p = 2; p > 0; p = sieve.nextPrime(p)) {
                for (int n = p; n <= limit; n += p) {
                    int a = exponent(n, p);
                    if (accum[n] == 0) {
                        accum[n] = a + 1;
                    } else {
                        accum[n] *= a + 1;
                    }
                }
            }

            int s = 0;
            for (int n = 2; n < limit; n++) {
                if (accum[n] == accum[n+1])
                    s++;
                accum[n] = s;
            }

            this.accum = accum;
        }

        public int solve(int n) {
            return accum[n - 1];
        }
    }

    public static int solve(int limit) {
        return new Solver(limit).solve(limit);
    }

    public static void main(String[] args) {
        int limit = 10_000_000;
        if (args.length > 0)
            limit = Integer.parseInt(args[0]);
        System.out.println(solve(limit));
    }
}
