package euler.algo;

import static euler.algo.Library.isqrt;
import static java.lang.Math.abs;
import static java.lang.Math.ceil;
import static java.lang.Math.log;
import static java.lang.Math.sqrt;

public class PrimeCounter {
    private static final int[] smallPrimes = {0, 2, 3, 5, 7, 11, 13, 17, 19, 23, 29};

    private static final double EPS = 1e-20;

    /**
     * Calculate the logarithmic integral using Ramanujan's formula:
     * https://en.wikipedia.org/wiki/Logarithmic_integral_function#Series_representation
     */
    private static double li(double x) {
        double gamma = 0.57721566490153286061;
        double sum = 0;
        double inner_sum = 0;
        double factorial = 1;
        double p = -1, q;
        double power2 = 1;
        double term;
        int k = 0;

        for (int n = 1; n < 200; n++) {
            p *= -log(x);
            factorial *= n;
            q = factorial * power2;
            power2 *= 2;
            for (; k <= (n - 1) / 2; k++)
                inner_sum += 1.0 / (2 * k + 1);
            term = (p / q) * inner_sum;
            sum += term;
            if (abs(term) < EPS)
                break;
        }

        return gamma + log(log(x)) + sqrt(x) * sum;
    }

    /**
     * Calculate the offset logarithmic integral which is a very accurate
     * approximation of the number of primes <= x.
     *
     * Li(x) > π(x) for 24 ≤ x ≤ ~ 10^316
     */
    public static long Li(long x) {
        if (x < 2)
            return 0;
        return (long)(li(x) - 1.04516378011749278484);
    }

    /**
     * Returns the approximation of the nth prime.
     */
    public static long approxPrime(long n) {
        if (n < smallPrimes.length)
            return smallPrimes[(int)n];

        double logn = log(n), log2n = log(logn);
        double upper;

        if (n >= 688383)
            upper = n * (logn + log2n - 1.0 + ((log2n - 2.0)/logn));
        else if (n >= 178974)
            upper = n * (logn + log2n - 1.0 + ((log2n - 1.95)/logn));
        else if (n >= 39017)
            upper = n * (logn + log2n - 0.9484);
        else
            upper = n * (logn + 0.6 * log2n);
        return (long)ceil(upper);
    }

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
