package euler.algo;

import java.util.BitSet;
import static euler.algo.Library.isqrt;

public class PrimeSieve implements Sieve {
    private final int limit;
    private final BitSet primes;

    public PrimeSieve(int limit) {
        this.limit = limit;
        this.primes = new BitSet(limit + 1);

        primes.set(2);
        for (int i = 3; i <= limit; i += 2) {
            primes.set(i);
        }

        int crossto = isqrt(limit);
        for (int p = 3; p > 0 && p <= crossto; p = primes.nextSetBit(p + 1)) {
            for (int n = p * p; n <= limit; n += p + p)
                primes.clear(n);
        }
    }

    @Override
    public int getLimit() {
        return limit;
    }

    @Override
    public boolean isPrime(int n) {
        return primes.get(n);
    }

    @Override
    public int nextPrime(int n) {
        return primes.nextSetBit(n + 1);
    }

    @Override
    public int previousPrime(int n) {
        return primes.previousSetBit(n - 1);
    }

    @Override
    public int cardinality() {
        return primes.cardinality();
    }

    @Override
    public int[] getPrimes() {
        int[] result = new int[cardinality()];
        for (int i = 0, p = 2; p > 0; p = nextPrime(p))
            result[i++] = p;
        return result;
    }
}
