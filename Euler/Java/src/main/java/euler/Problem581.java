package euler;

import euler.algo.Pair;
import euler.algo.PellEquation;
import euler.algo.PrimeSieve;

public final class Problem581 {
    private Problem581() {}

    public static long solve(int limit) {
        int[] primes = new PrimeSieve(limit).getPrimes();
        int k = Math.max(3, (limit + 1) / 2);
        long sum = 0;

        for (int mask = 0; mask < (1 << primes.length); mask++) {
            long q = 1;
            int b = mask;

            // get a square free smooth number
            for (int i = 0; b != 0; i++) {
                int skip = Integer.numberOfTrailingZeros(b);
                i += skip;
                q *= primes[i];
                b >>= skip + 1;
            }

            // omit 2 from smooth numbers
            if (q == 2)
                continue;

            // solve Pell equation x^2 - 2qy^2 = 1 for all q in smooth numbers
            int i = 0;
            for (Pair p : PellEquation.series(2 * q, 1)) {
                long n = (p.x - 1) / 2;
                if (isSmooth(n, primes) && isSmooth(n + 1, primes))
                    sum += n;
                if (++i == k)
                    break;
            }
        }

        return sum;
    }

    private static boolean isSmooth(long n, int[] primes) {
        for (int p : primes)
            while (n % p == 0)
                n /= p;
        return n == 1;
    }

    public static void main(String[] args) {
        System.out.println(solve(47));
    }
}
