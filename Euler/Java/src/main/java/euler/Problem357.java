package euler;

import euler.algo.PrimeSieve;
import euler.util.LongRangedTask;
import static euler.algo.Library.isqrt;

public final class Problem357 {
    private Problem357() {}

    public static long solve(int limit) {
        PrimeSieve sieve = new PrimeSieve(limit);
        return LongRangedTask.parallel(2, limit, (from, to) -> {
            long sum = 0;
            for (int i = from; i <= to; i++) {
                if (sieve.isPrime(i) && check(sieve, i - 1))
                    sum += i - 1;
            }
            return sum;
        });
    }

    private static boolean check(PrimeSieve sieve, int n) {
        for (int i = 1, sq = isqrt(n); i <= sq; i++)
            if (n % i == 0 && !sieve.isPrime(i + n / i))
                return false;
        return true;
    }

    public static void main(String[] args) {
        System.out.println(solve(100_000_000));
    }
}