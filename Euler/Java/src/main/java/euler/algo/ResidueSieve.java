package euler.algo;

import java.util.BitSet;

import static euler.algo.Library.isqrt;
import static euler.algo.Library.mod;
import static euler.algo.Library.modinv;

/**
 * A special prime sieve that sieve primes in the given residue class.
 */
public class ResidueSieve {
    private final long modulo, start;
    private final BitSet sieve;
    private int next;

    public ResidueSieve(int modulo, int residue, long start, long limit) {
        // adjust actual start
        residue = mod(residue, modulo);
        start = ((start - residue) / modulo + 1) * modulo + residue;

        // get number of elements in the range
        int length = (int)((limit - start) / modulo + 1);

        // allocate and initialize prime sieve
        sieve = new BitSet(length);

        // we still need lower primes to cross higher primes
        PrimeSieve init = new PrimeSieve((int)isqrt(limit));

        for (int p = 2; p > 0; p = init.nextPrime(p)) {
            if (modulo % p == 0)
                continue; // already excluded

            // find the first composite needs to cross
            int n = mod(-start * modinv(modulo, p), p);
            if ((long)n * modulo + start == p)
                n += p; // don't cross the prime itself

            // crossing composites
            while (n < length) {
                sieve.set(n);
                n += p;
            }
        }

        this.modulo = modulo;
        this.start = start;
        this.next = sieve.nextClearBit(0);
    }

    /**
     * Returns the next prime in residue class.
     */
    public long next() {
        if (next < 0)
            return -1;
        long res = next * modulo + start;
        next = sieve.nextClearBit(next + 1);
        return res;
    }
}
