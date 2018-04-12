package euler.util;

import java.util.BitSet;

public final class PrimeSieve {
    private PrimeSieve() {}

    public static BitSet build(int limit) {
        BitSet primes = new BitSet(limit + 1);

        primes.set(2);
        for (int i = 3; i <= limit; i += 2) {
            primes.set(i);
        }

        int sq = (int)Math.sqrt(limit);
        for (int i = 3; i <= sq; i += 2) {
            if (primes.get(i))
                for (int j = i + i; j <= limit; j += i)
                    primes.clear(j);
        }

        return primes;
    }
}
