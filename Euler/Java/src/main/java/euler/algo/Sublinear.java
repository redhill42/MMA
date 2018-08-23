package euler.algo;

import java.util.Arrays;

import static euler.algo.Library.isqrt;
import static euler.algo.Library.isqrtExact;
import static euler.algo.Library.mod;
import static euler.algo.Library.tri;
import static java.lang.Math.log;

public final class Sublinear {
    private Sublinear() {}

    /**
     * Sublinear computation result. The results are separated in two arrays.
     * The first array contains small values that can be accessed directly
     * with an index. The second array contains large values that must be
     * accessed with index (n/i).
     */
    public static class Result {
        private final long n;
        private final long[] small;
        private final long[] large;

        Result(long n, long[] small, long[] large) {
            this.n = n;
            this.small = small;
            this.large = large;
        }

        public long get(long i) {
            return (i < small.length) ? small[(int)i] : large[(int)(n / i)];
        }
    }

    /**
     * Returns an array that contains moebius μ function.
     */
    public static byte[] moebius(int L) {
        return moebius(new PrimeSieve(L), L);
    }

    public static byte[] moebius(Sieve sieve, int L) {
        byte[] mu = new byte[L + 1];

        Arrays.fill(mu, (byte)1);
        mu[0] = 0;

        for (int p = 2; p > 0; p = sieve.nextPrime(p)) {
            // μ(n) = (-1)^k if n is a product of k distinct primes
            for (int n = p; n <= L; n += p)
                mu[n] = (byte)-mu[n];

            // μ(n) = 0 when n is not square-free
            long q = (long)p * p;
            for (long n = q; n <= L; n += q)
                mu[(int)n] = 0;
        }

        return mu;
    }

    public static class MertensResult extends Result {
        private final byte[] term;

        MertensResult(long n, byte[] mu, long[] small, long[] large) {
            super(n, small, large);
            this.term = mu;
        }

        public int term(int i) {
            return term[i];
        }
    }

    /**
     * Compute the Mertens function.
     */
    public static MertensResult mertensList(long n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n <= 0");
        }

        long L;
        if (n < 1000) {
            L = n;
        } else {
            L = (long)Math.pow(n / log(log(n)), 2.0/3.0);
            if (L >= Integer.MAX_VALUE || n / L >= Integer.MAX_VALUE) {
                throw new IllegalArgumentException("overflow");
            }
        }

        byte[] mu = moebius((int)L);
        long[] small = new long[(int)L + 1];
        long[] large = new long[n == L ? 0 : (int)(n / L + 1)];

        for (int i = 1; i <= L; i++) {
            small[i] = small[i-1] + mu[i];
        }

        for (int x = large.length - 1; x >= 1; x--) {
            long k = n / x, maxk = isqrt(k);
            long res = 1 - (k + 1) / 2;
            for (int z = 2; z <= maxk; z++) {
                long i = k / z;
                res -= (i <= L) ? small[(int)i] : large[x * z];
                if (z != i) {
                    res -= (i - k / (z + 1)) * small[z];
                }
            }
            large[x] = res;
        }

        return new MertensResult(n, mu, small, large);
    }

    public static long mertens(long n) {
        return mertensList(n).get(n);
    }

    private static final int[] SMALL = {
        0, 1, 2, 3, 3, 4, 5, 6, 6, 6, 7
    };

    /**
     * Efficient counting square-free numbers.
     *
     * Reference: https://arxiv.org/pdf/1107.4890.pdf
     */
    public static long sqfn(long n) {
        if (n < 0)
            return 0;
        if (n < SMALL.length)
            return SMALL[(int)n];

        int  I = (int)(Math.pow(n, 0.2) * Math.pow(log(log(n)), 0.8));
        int  D = (int)isqrtExact(n / I);
        int  B = isqrtExact(D);
        long S = 0;

        PrimeSieve sieve = new PrimeSieve(B);
        byte[] mu = new byte[B];
        long[] M1 = new long[D + 1];
        long[] M2 = new long[I + 1];

        // compute S1(n) and M(d) for d = 1,...,D
        int a = 1;
        do {
            int b = Math.min(a + B - 1, D);
            tabulateMoebiusBlock(mu, sieve, a, b);
            for (int k = a; k <= b; k++) {
                int m = mu[k - a];
                if (m != 0)
                    S += m * (n/k/k);
                M1[k] = M1[k-1] + m;
            }
            a = b + 1;
        } while (a <= D);

        // compute M(x_i) by (9)
        for (int i = I; i >= 1; i--) {
            long x = isqrtExact(n / i);
            long t = 1 - (x + 1) / 2;
            int  maxd = (int)isqrtExact(x);
            for (int d = 2; d <= maxd; d++) {
                long j = x / d;
                t -= (j <= D) ? M1[(int)j] : M2[d*d*i];
                if (d != j)
                    t -= (j - x / (d + 1)) * M1[d];
            }
            M2[i] = t;
        }

        // compute S2(n) by (8)
        for (int i = 1; i < I; i++)
            S += M2[i];
        S -= (I - 1) * M2[I];

        return S;
    }

    /**
     * Computing values of the Moebius function: memory efficient sieving in blocks.
     *
     * Require: bounds 0 < a <= b
     * Ensure: μ(k) = mu[k] for each k ∈ [a, b]
     */
    private static void tabulateMoebiusBlock(byte[] mu, PrimeSieve sieve, long a, long b) {
        long[] m = new long[(int)(b - a + 1)];
        Arrays.fill(mu, (byte)1);
        Arrays.fill(m, 1);

        int crossto = (int)isqrtExact(b);
        for (int p = 2; p > 0 && p <= crossto; p = sieve.nextPrime(p)) {
            for (long k = (a + p - 1) / p * p; k <= b; k += p) {
                int i = (int)(k - a);
                mu[i] = (byte)-mu[i];
                m[i] *= p;
            }

            long q = (long)p * p;
            for (long k = (a + q - 1) / q * q; k <= b; k += q) {
                int i = (int)(k - a);
                mu[i] = 0;
            }
        }

        for (long k = a; k <= b; k++) {
            int i = (int)(k - a);
            if (m[i] < k)
                mu[i] = (byte)-mu[i];
        }
    }

    public static class TotientSumResult extends Result {
        private final TotientSieve phi;

        TotientSumResult(long n, TotientSieve phi, long[] small, long[] large) {
            super(n, small, large);
            this.phi = phi;
        }

        public int term(int i) {
            return phi.phi(i);
        }
    }

    /**
     * Compute list of totient summation up to the given number.
     *
     * @return list of totient summation up to the given number
     */
    public static TotientSumResult totientSumList(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n <= 0");
        }

        int L;
        if (n < 1000) {
            L = n;
        } else {
            L = (int)(Math.pow(n / log(log(n)), 2.0/3.0));
        }

        TotientSieve sieve = new TotientSieve(L);
        long[] small = new long[L + 1];
        long[] large = new long[n == L ? 0 : n / L + 1];

        for (int i = 1; i <= L; i++) {
            small[i] = small[i-1] + sieve.phi(i);
        }

        for (int x = large.length - 1; x >= 1; x--) {
            int k = n / x, maxk = isqrt(k);
            long res = (long)k * (k + 1) / 2 - (k + 1) / 2;
            for (int z = 2; z <= maxk; z++) {
                int i = k / z;
                res -= (i <= L) ? small[i] : large[x * z];
                if (z != i) {
                    res -= (i - k / (z + 1)) * small[z];
                }
            }
            large[x] = res;
        }

        return new TotientSumResult(n, sieve, small, large);
    }

    /**
     * Compute the totient summation up to the given number.
     *
     * @return the totient summation up to the given number
     */
    public static long totientSum(int n) {
        return totientSumList(n).get(n);
    }

    /**
     * Compute list of totient summation modulo m up to n.
     *
     * @return list of totient summation modulo m up to n
     */
    public static TotientSumResult totientModSumList(long n, long m) {
        if (n <= 0) {
            throw new IllegalArgumentException("n <= 0");
        }

        long L;
        if (n < 1000) {
            L = n;
        } else {
            L = (long)Math.pow(n / log(log(n)), 2.0/3.0);
            if (L >= Integer.MAX_VALUE || n / L >= Integer.MAX_VALUE) {
                throw new IllegalArgumentException("overflow");
            }
        }

        TotientSieve sieve = new TotientSieve((int)L);
        long[] small = new long[(int)L + 1];
        long[] large = new long[n == L ? 0 : (int)(n / L + 1)];

        for (int i = 1; i <= L; i++) {
            small[i] = (small[i-1] + sieve.phi(i)) % m;
        }

        for (int x = large.length - 1; x >= 1; x--) {
            long k = n / x, maxk = isqrt(k);
            long res = tri(k, m);
            res -= (k + 1) / 2;
            for (int z = 2; z <= maxk; z++) {
                long i = k / z;
                res -= (i <= L) ? small[(int)i] : large[x * z];
                if (z != i) {
                    res -= (i - k / (z + 1)) * small[z];
                }
            }
            large[x] = mod(res, m);
        }

        return new TotientSumResult(n, sieve, small, large);
    }
}
