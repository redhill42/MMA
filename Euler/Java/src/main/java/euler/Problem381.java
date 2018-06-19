package euler;

import euler.algo.PrimeSieve;
import euler.util.LongRangedTask;

public final class Problem381 {
    private Problem381() {}

    public static long solve(int limit) {
        PrimeSieve sieve = new PrimeSieve(limit);
        return LongRangedTask.parallel(5, limit - 1, 1000, (from, to) -> {
            long sum = 0;
            int p = sieve.nextPrime(from - 1);
            while (p > 0 && p <= to) {
                sum += ((3 * p % 8) * p - 3) / 8;
                p = sieve.nextPrime(p);
            }
            return sum;
        });
    }

    public static void main(String[] args) {
        int limit = 100_000_000;
        if (args.length > 0)
            limit = Integer.parseInt(args[0]);
        System.out.println(solve(limit));
    }
}
