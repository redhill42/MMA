package euler;

import java.util.BitSet;

import euler.algo.FactorizationSieve;

public final class Problem23 {
    private Problem23() {}

    private static final int LIMIT = 28123;

    public static int solve() {
        // Find all abundant numbers
        FactorizationSieve sieve = new FactorizationSieve(LIMIT + 1);
        int[] abundants = new int[LIMIT + 1];
        int k = 0;

        for (int n = 1; n <= LIMIT; n++)
            if (sieve.sigma(1, n) > n + n)
                abundants[k++] = n;

        // Sum pair of abundant numbers
        BitSet absums = new BitSet(LIMIT + 1);
        for (int i = 0; i < k; i++) {
            for (int j = i; j < k; j++) {
                int n = abundants[i] + abundants[j];
                if (n > LIMIT)
                    break;
                absums.set(n);
            }
        }

        // Find all numbers that cannot be written as the sum of two abundant numbers
        int res = 0;
        for (int n = 1; n <= LIMIT; n++)
            if (!absums.get(n))
                res += n;
        return res;
    }

    public static void main(String[] args) {
        System.out.println(solve());
    }
}
