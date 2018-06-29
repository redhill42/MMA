package euler;

import euler.algo.PrimeSieve;
import static euler.algo.Library.fibonacci;

public final class Problem618 {
    private Problem618() {}

    public static long solve(int n, int m) {
        int limit = (int)fibonacci(n);
        PrimeSieve sieve = new PrimeSieve(limit);
        long[] d = new long[limit + 1];

        d[0] = 1;
        for (int p = 2; p > 0; p = sieve.nextPrime(p)) {
            for (int k = p; k <= limit; k++) {
                d[k] = (d[k] + d[k - p] * p) % m;
            }
        }

        long s = 0;
        for (int i = 2; i <= n; i++)
            s += d[(int)fibonacci(i)];
        return s % m;
    }

    public static void main(String[] args) {
        int n = 24;
        if (args.length > 0)
            n = Integer.parseInt(args[0]);
        System.out.println(solve(n, 1_000_000_000));
    }
}
