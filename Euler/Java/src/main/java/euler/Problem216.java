package euler;

import java.util.BitSet;

import euler.algorithms.TonelliShanks;
import euler.util.PrimeSieve;

public final class Problem216 {
    private Problem216() {}

    public static int solve(int limit) {
        PrimeSieve primes = new PrimeSieve((int)(1.42 * limit));
        BitSet t = new BitSet(limit + 1);
        t.set(1);

        for (int p = 2; p > 0; p = primes.nextPrime(p)) {
            if (p % 8 == 1 || p % 8 == 7) {
                int r = TonelliShanks.solve((p + 1) / 2, p);
                if (r == -1)
                    continue;
                if (r > p / 2)
                    r = p - r;
                for (int m = p; m - r <= limit; m += p) {
                    t.set(m - r);
                    if (m + r <= limit)
                        t.set(m + r);
                }
            }
        }
        return limit - t.cardinality();
    }

    public static void main(String[] args) {
        int n = 50_000_000;
        if (args.length > 0)
            n = Integer.parseInt(args[0]);
        System.out.println(solve(n));
    }
}
