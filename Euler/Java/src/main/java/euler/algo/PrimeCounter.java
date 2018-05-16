package euler.algo;

import static euler.algo.Library.isqrt;
import static java.lang.Math.abs;

public class PrimeCounter {
    private final int[] primes;
    private final PrimePi pi;
    private final PhiCache cache;

    public PrimeCounter(long limit) {
        SegmentedSieve sieve = new SegmentedSieve(limit);
        SegmentedSieve.Segment seg = sieve.segment();

        int n = (int)isqrt(limit);
        primes = new int[n];
        for (int i = 0; i < n; i++)
            primes[i] = (int)seg.next();
        pi = new PrimePi(primes);
        cache = new PhiCache();
    }

    int prime(int i) {
        return primes[i - 1]; // Return 1-based nth prime.
    }

    int pi(int x) {
        assert x <= pi.getLimit();
        return pi.pi(x);
    }

    int pi(long x) {
        assert x <= pi.getLimit();
        return pi.pi((int)x);
    }

    /**
     * phi(x, a) counts the numbers <= x that are not divisible by any of
     * the first a primes. TinyPhi computes phi(x, a) in a constant time
     * for a <= 6 using lookup tables.
     *
     * phi(x, a) = (x / pp) * φ(a) + phi(x % pp, a - 1)
     * pp = 2 * 3 * ... * prime[a]
     * where φ(a) is Euler totient function
     */
    private static class TinyPhi {
        private static final int[] primes = {0, 2, 3, 5, 7, 11, 13};
        private static final int[] prime_products = {1, 2, 6, 30, 210, 2310, 30030};
        private static final int[] totients = {1, 1, 2, 8, 48, 480, 5760};
        private static final int[] pi = {0, 0, 1, 2, 2, 3, 3, 4, 4, 4, 4, 5, 5};

        private static final int MAX_A = primes.length - 1;

        private final int[][] phi = new int[7][];

        public TinyPhi() {
            phi[0] = new int[]{0};

            // initialize phi(x % pp, a) lookup tables
            for (int a = 1; a <= MAX_A; a++) {
                int pp = prime_products[a];
                phi[a] = new int[pp];
                for (int x = 0; x < pp; x++) {
                    long phi_xa = phi(x, a - 1) - phi(x / primes[a], a - 1);
                    phi[a][x] = (int)phi_xa;
                }
            }
        }

        public boolean isTiny(long a) {
            return a <= MAX_A;
        }

        public long phi(long x, int a) {
            assert a <= MAX_A;
            int pp = prime_products[a];
            return (x / pp) * totients[a] + phi[a][(int)(x % pp)];
        }

        public int getC(long y) {
            return y >= primes[MAX_A] ? MAX_A : pi[(int)y];
        }
    }

    private static final TinyPhi tinyPhi = new TinyPhi(); // singleton

    /**
     * The PhiCache class calculates the partial sieve function (a.k.a. Legendre-sum)
     * using the recursive formula:
     *      phi(x, a) = phi(x, a - 1) - phi(x / primes[a], a - 1).
     * phi(x, a) counts the numbers <= x that are not divisible by any of the first
     * a primes.
     */
    private class PhiCache {
        // cache phi(x, a) results if a < MAX_A
        private static final int MAX_A = 100;
        private static final int MAX_X = 10000;

        private final long[][] cache = new long[MAX_A][];

        // Calculate phi(x, a) using the recursive formula:
        //     phi(x, a) = phi(x, a - 1) - phi(x / primes[a], a - 1)
        long phi(long x, int a, int sign) {
            if (x <= prime(a))
                return sign;
            if (tinyPhi.isTiny(a))
                return tinyPhi.phi(x, a) * sign;
            if (isPix(x, a))
                return (pi(x) - a + 1) * sign;
            if (isCached(x, a))
                return cache[a][(int)x] * sign;

            long sqrtx = isqrt(x);
            int  b = a;
            int  c = tinyPhi.getC(sqrtx);
            long sum = 0;

            if (sqrtx <= pi.getLimit())
                b = Math.min(pi(sqrtx), a);

            sum += (b - a) * sign;
            sum += tinyPhi.phi(x, c) * sign;

            for (int i = c; i < b; i++) {
                long x2 = x / prime(i+1);
                sum += phi(x2, i, -sign);
            }

            updateCache(x, a, sum);
            return sum;
        }

        private boolean isPix(long x, int a) {
            return x <= pi.getLimit() &&
                   x < (long)prime(a+1) * prime(a+1);
        }

        private boolean isCached(long x, int a) {
            return a < MAX_A &&
                   cache[a] != null &&
                   x < cache[a].length &&
                   cache[a][(int)x] != 0;
        }

        private void updateCache(long x, int a, long sum) {
            if (a < MAX_A && x < MAX_X) {
                if (cache[a] == null)
                    cache[a] = new long[MAX_X];
                cache[a][(int)x] = abs(sum);
            }
        }
    }

    private long phi(long x, int a) {
        if (x < 1) return 0;
        if (a > x) return 1;
        if (a < 1) return x;

        if (tinyPhi.isTiny(a))
            return tinyPhi.phi(x, a);
        if (prime(a) >= x)
            return 1;

        int sqrtx = (int)isqrt(x);
        int c = tinyPhi.getC(sqrtx);
        int b = Math.min(pi(sqrtx), a);

        long sum = tinyPhi.phi(x, c) - a + b;
        for (int i = c; i < b; i++)
            sum += cache.phi(x / prime(i+1), i, -1);
        return sum;
    }

    private long legendre(long x) {
        if (x <= pi.getLimit()) {
            return pi(x);
        } else {
            int a = (int)legendre(isqrt(x));
            return phi(x, a) + a - 1;
        }
    }

    public long countPrimes(long x) {
        return legendre(x);
    }
}
