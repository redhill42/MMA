package euler.algo;

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
}
