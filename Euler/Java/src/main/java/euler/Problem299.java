package euler;

import euler.util.LongRangedTask;

import static euler.algo.Library.isCoprime;
import static euler.algo.Library.isqrt;

public final class Problem299 {
    private Problem299() {}

    public static long solve(int limit) {
        return incenter(limit - 1) + parallel(limit - 1);
    }

    private static long incenter(int limit) {
        int max_m = isqrt(2 * (limit + 1)) - 1;
        return LongRangedTask.parallel(1, max_m, (from, to) -> {
            long count = 0;
            for (long m = from | 1; m <= to; m += 2) {
                for (long n = 1; n < m; n += 2) {
                    if (isCoprime(m, n)) {
                        long s = m*n + (m*m - n*n) / 2;
                        if (s > limit) break;
                        count += 2 * (limit / s);
                    }
                }
            }
            return count;
        });
    }

    private static long parallel(int limit) {
        int max_m = isqrt(limit / 2 - 1) - 1;
        return LongRangedTask.parallel(1, max_m, (from, to) -> {
            long count = 0;
            for (long m = from | 1; m <= to; m += 2) {
                for (long n = 1; ; n++) {
                    if (isCoprime(m, n)) {
                        long s = 2 * (2*m*n + m*m + 2*n*n);
                        if (s > limit) break;
                        count += limit / s;
                    }
                }
            }
            return count;
        });
    }

    public static void main(String[] args) {
        int n = 100_000_000;
        if (args.length > 0)
            n = Integer.parseInt(args[0]);
        System.out.println(solve(n));
    }
}
