package euler.algo;

/**
 * The PrimePi class is a compressed lookup table for small prime counts.
 * It uses only (n / 4) bytes of memory and returns the number of
 * primes <= n in O(1) operations. It uses Eratosthenes sieve to counting
 * primes, so it can not be used for very large numbers.
 */
public class PrimePi {
    private final int limit;
    private final int[] count;
    private final long[] bits;

    public PrimePi(int limit) {
        this(new PrimeSieve(limit));
    }

    public PrimePi(PrimeSieve sieve) {
        this.limit = sieve.getLimit();
        this.count = new int[limit / 64 + 1];
        this.bits = new long[limit / 64 + 1];

        for (int p = 2; p > 0; p = sieve.nextPrime(p)) {
            bits[p / 64] |= 1L << (p % 64);
        }

        int pix = 0;
        for (int i = 0; i < count.length; i++) {
            count[i] = pix;
            pix += Long.bitCount(bits[i]);
        }
    }

    public PrimePi(int[] primes) {
        this(primes, primes[primes.length - 1]);
    }

    public PrimePi(int[] primes, int limit) {
        this.limit = limit;
        this.count = new int[limit / 64 + 1];
        this.bits = new long[limit / 64 + 1];

        for (int p : primes) {
            if (p > limit)
                break;
            bits[p / 64] |= 1L << (p % 64);
        }

        int pix = 0;
        for (int i = 0; i < count.length; i++) {
            count[i] = pix;
            pix += Long.bitCount(bits[i]);
        }
    }

    public int getLimit() {
        return limit;
    }

    public int pi(int n) {
        long bitmask = -1L >>> (63 - n % 64);
        return count[n / 64] + Long.bitCount(bits[n / 64] & bitmask);
    }
}
