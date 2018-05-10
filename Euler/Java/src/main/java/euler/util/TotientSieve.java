package euler.util;

import static euler.util.Utils.isqrt;

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

        for (int p = 3; p < limit; p += 2) {
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

    private static final int[] totientSumCache = {
        0, 1, 2, 4, 6, 10, 12, 18, 22, 28, 32
    };

    /**
     * Compute the totient summation up to the given number.
     *
     * @return the totient summation up to the given number
     */
    public static long sum(int n) {
        if (n < 0)
            return 0;
        if (n < totientSumCache.length)
            return totientSumCache[n];

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
            long k = n / x, klimit = isqrt(k);
            long res = k * (k + 1) / 2;

            for (int g = 2; g <= klimit; g++) {
                if (k / g <= L) {
                    res -= sieve[(int)(k / g)];
                } else {
                    res -= bigV[x * g];
                }
            }

            for (int z = 1; z <= klimit; z++) {
                if (z != k / z)
                    res -= (k / z - k / (z + 1)) * sieve[z];
            }

            bigV[x] = res;
        }

        return bigV[1];
    }
}
