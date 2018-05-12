package euler;

import euler.util.PrimeSieve;
import static euler.util.Utils.even;
import static euler.util.Utils.pow;

public final class Problem268 {
    private Problem268() {}

    public static long solve(int maxPrime, int distinctPrimes, long limit) {
        int[] primes = new PrimeSieve(maxPrime).getPrimes();
        int numPrimes = primes.length;
        if (numPrimes > 63)
            throw new IllegalArgumentException("Unsolvable with current algorithm");

        // precompute tetrahedral numbers
        int[] choose = new int[numPrimes + 1];
        for (int i = distinctPrimes; i <= numPrimes; i++)
            choose[i] = tetrahedral(i - distinctPrimes + 1);

        long bitlimit = (1L << primes.length) - 1;
        long sum = 0;

        for (long bitmask = 0; bitmask <= bitlimit; bitmask++) {
            int nbits = Long.bitCount(bitmask);
            if (nbits < distinctPrimes)
                continue;

            long product = product(primes, bitmask, limit);
            if (product >= limit) {
                // optimization: if the next mask has the same number of bits
                // set (or more), then it will exceed the limit as well
                long lowestBit = Long.lowestOneBit(bitmask);
                do {
                    bitmask += lowestBit;
                    lowestBit <<= 1;
                } while ((bitmask & lowestBit) != 0);
                bitmask--; // add back at for-loop
                continue;
            }

            // count numbers divisible by the current primes, avoid
            // overlaps by inclusion-exclusion principle
            long count = (limit / product) * choose[nbits];
            sum = even(nbits) ? sum + count : sum - count;
        }
        return sum;
    }

    private static int tetrahedral(int n) {
        // see https://en.wikipedia.org/wiki/Tetrahedral_number
        return n * (n + 1) * (n + 2) / 6;
    }

    private static long product(int[] primes, long bitmask, long limit) {
        long result = 1L;
        int i = 0;
        while (bitmask != 0 && result < limit) {
            if ((bitmask & 1) != 0)
                result = mul(result, primes[i]);
            bitmask >>= 1;
            i++;
        }
        return result;
    }

    private static final long LO_MASK = 0xFFFFFFFFL;
    private static final int HI_SHIFT = 32;

    private static long mul(long x, long y) {
        if ((x | y) >>> 31 == 0)
            return x * y;

        long x0 = x & LO_MASK;
        long x1 = x >>> HI_SHIFT;
        long y0 = y & LO_MASK;
        long y1 = y >>> HI_SHIFT;
        long t  = (x1 * y0) + (x0 * y0 >>> HI_SHIFT);
        long w1 = t & LO_MASK;
        long w2 = t >>> HI_SHIFT;

        long u = (x1 * y1 + w2) + ((x0 * y1 + w1) >>> HI_SHIFT);
        long v = x * y;
        return (u == 0 && v >= 0) ? v : Long.MAX_VALUE;
    }

    public static void main(String[] args) {
        int e = 16;
        if (args.length > 0)
            e = Integer.parseInt(args[0]);
        System.out.println(solve(100, 4, pow(10L, e)));
    }
}
