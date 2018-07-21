package euler;

import euler.algo.SegmentedSieve;
import euler.util.LongRangedTask;

import static euler.algo.Library.mod;
import static euler.algo.Library.modmul;
import static euler.algo.Library.modpow;

public final class Problem487 {
    private Problem487() {}

    public static long solve(int k, long n, long p1, long p2) {
        long[] primes = new SegmentedSieve(p2).segment(p1, p2).getPrimes();
        return LongRangedTask.parallel(0, primes.length - 1, 0, (from, to) -> {
            long sum = 0;
            for (int i = from; i <= to; i++) {
                long p = primes[i];
                long s = 0;
                for (long j = n % p + 1; j <= p; j++)
                    s -= modmul(n - j + 1, modpow(j, k, p), p);
                sum += mod(s, p);
            }
            return sum;
        });
    }

    public static void main(String[] args) {
        System.out.println(solve(10000, (long)1e12, (long)2e9, (long)2e9+2000));
    }
}
