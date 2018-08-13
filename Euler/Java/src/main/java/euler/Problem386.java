package euler;

import java.util.HashMap;
import java.util.Map;

import euler.algo.PrimeSieve;

public final class Problem386 {
    private Problem386() {}

    private static final int  BITS = 5;
    private static final long MASK = (1L << BITS) - 1;
    private static final int  MAX_EXPONENTS = 64 / BITS;

    private static class Solver {
        private final PrimeSieve sieve;
        private final Map<Long,Integer> memo = new HashMap<>();

        Solver(int limit) {
            sieve = new PrimeSieve(limit);
        }

        long solve() {
            return search(2, 0, 1, sieve.getLimit()) + 1;
        }

        long search(int p, long a, long n, long limit) {
            long sum = 0;
            for (; p > 0; p = sieve.nextPrime(p)) {
                long next = n * p;
                if (next > limit)
                    break;
                for (int b = 1; next <= limit; b++, next *= p) {
                    // Use a base-32 long integer to represent exponents,
                    // for example, the exponents 2^3*3^4*5^2 is represented
                    // as 342 in base-32 representation. Since the limit is
                    // not very large, there is enough room to represent all
                    // exponent combinations.
                    long e = (a << BITS) | b;
                    sum += antichain(e);
                    sum += search(sieve.nextPrime(p), e, next, limit);
                }
            }
            return sum;
        }

        private int antichain(long hash) {
            return memo.computeIfAbsent(hash, x -> {
                int[] exponents = new int[MAX_EXPONENTS];
                int numExponents = 0;
                int sum = 0;

                for (long h = hash; h != 0; h >>>= BITS) {
                    int e = (int)(h & MASK);
                    exponents[numExponents++] = e;
                    sum += e;
                }

                return compute(exponents, 0, numExponents, sum / 2);
            });
        }

        /**
         * A set of integers where each integer is the product of an equal
         * number of primes is an antichain.
         */
        private static int compute(int[] exponents, int from, int to, int n) {
            if (from >= to)
                return n == 0 ? 1 : 0;
            if (n < 0)
                return 0;

            int k = exponents[from++];
            if (from >= to)
                return k >= n ? 1 : 0;

            int res = 0;
            for (int i = 0; i <= k; i++)
                res += compute(exponents, from, to, n - i);
            return res;
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
