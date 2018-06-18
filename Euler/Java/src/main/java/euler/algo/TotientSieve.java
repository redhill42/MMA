package euler.algo;

import static euler.algo.Library.even;
import static euler.algo.Library.isqrt;
import static euler.algo.Library.mod;
import static euler.algo.Library.modmul;

public class TotientSieve implements Sieve{
    private final int[] phi;
    private final int nprimes;

    public TotientSieve(int limit) {
        int[] phi = new int[limit + 1];
        int nprimes = 1;
        int n;

        phi[1] = phi[2] = 1;
        for (n = 3; n < limit; n += 2) {
            phi[n] = n;
            phi[n+1] = (n+1)/2;
        }
        if (n == limit) {
            phi[n] = n;
        }

        for (int p = 3; p <= limit; p += 2) {
            if (p == phi[p]) {
                phi[p] = p - 1;
                nprimes++;
                for (n = p + p; n <= limit; n += p) {
                    phi[n] -= phi[n] / p;
                }
            }
        }

        this.phi = phi;
        this.nprimes = nprimes;
    }

    public int phi(int n) {
        return phi[n];
    }

    @Override
    public int getLimit() {
        return phi.length - 1;
    }

    @Override
    public boolean isPrime(int n) {
        return phi[n] == n - 1;
    }

    @Override
    public int nextPrime(int n) {
        if (n < 2)
            return 2;
        if (++n % 2 == 0)
            ++n;
        for (; n < phi.length; n += 2)
            if (phi[n] == n - 1)
                return n;
        return -1;
    }

    @Override
    public int previousPrime(int n) {
        if (n < 0)
            n = phi.length;
        if (n == 3)
            return 2;
        if (n <= 2)
            return -1;
        if (--n % 2 == 0)
            --n;
        for (; n >= 3; n -= 2)
            if (phi[n] == n - 1)
                return n;
        return -1;
    }

    @Override
    public int cardinality() {
        return nprimes;
    }

    @Override
    public int[] getPrimes() {
        int[] result = new int[nprimes];
        int i = 0;

        result[i++] = 2;
        for (int n = 3; n < phi.length; n += 2)
            if (phi[n] == n - 1)
                result[i++] = n;
        return result;
    }

    private static double log2(double x) {
        return Math.log(x) / Math.log(2);
    }

    /**
     * Compute the totient summation up to the given number.
     *
     * @return the totient summation up to the given number
     */
    public static long totientSum(int n) {
        if (n < 0)
            return 0;
        if (n <= 2)
            return n;

        int L = (int)(Math.pow(n / log2(log2(n)), 2.0/3.0));
        long[] sieve = new long[L + 1];
        long[] bigV  = new long[n / L + 1];

        for (int i = 0; i < sieve.length; i++)
            sieve[i] = i;
        for (int p = 2; p <= L; p++) {
            if (p == sieve[p])
                for (int k = p; k <= L; k += p)
                    sieve[k] -= sieve[k] / p;
            sieve[p] += sieve[p - 1];
        }

        for (int x = n / L; x >= 1; x--) {
            long k = n / x, maxk = isqrt(k);
            long res = k * (k + 1) / 2 - (k + 1) / 2;

            for (int z = 2; z <= maxk; z++) {
                long i = k / z;
                if (i <= L) {
                    res -= sieve[(int)i];
                } else {
                    res -= bigV[x * z];
                }
                if (z != i) {
                    res -= (i - k / (z + 1)) * sieve[z];
                }
            }
            bigV[x] = res;
        }

        return bigV[1];
    }

    public static long[] totientSumList(long n, long m, long L) {
        if (n < 0)
            return new long[2];
        if (n <= 2)
            return new long[]{0, n % m};

        if (L >= Integer.MAX_VALUE)
            throw new IllegalArgumentException("overflow");

        long[] sieve = new long[(int)(L + 1)];
        long[] bigV =  new long[(int)(n / L + 1)];

        for (int i = 0; i < sieve.length; i++)
            sieve[i] = i;
        for (int p = 2; p <= L; p++) {
            if (sieve[p] == p)
                for (int k = p; k <= L; k += p)
                    sieve[k] -= sieve[k] / p;
            sieve[p] += sieve[p - 1];
        }

        for (int x = (int)(n / L); x >= 1; x--) {
            long k = n / x, maxk = isqrt(k);
            long res = even(k) ? modmul(k>>1, k+1, m) : modmul(k, (k+1)>>1, m);

            res -= (k + 1) / 2;
            for (int z = 2; z <= maxk; z++) {
                long i = k / z;
                if (i <= L) {
                    res -= sieve[(int)i];
                } else {
                    res -= bigV[x * z];
                }
                if (z != i) {
                    res -= (i - k / (z + 1)) * sieve[z];
                }
            }
            bigV[x] = mod(res, m);
        }

        return bigV;
    }

    public static long totientSumMod(long n, long m) {
        long L = (long)(Math.pow(n / log2(log2(n)), 2.0/3.0));
        return totientSumList(n, m, L)[1];
    }
}
