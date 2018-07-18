package euler;

import euler.util.LongRangedTask;

import static euler.algo.Library.isCoprime;
import static java.lang.Math.sqrt;

public final class Problem279 {
    private Problem279() {}

    public static long solve(long limit) {
        return triangle90(limit) + triangle60(limit) + triangle120(limit);
    }

    private static long triangle90(long limit) {
        int max_m = (int)((sqrt(4*limit + 1) - 1) / 2);
        return LongRangedTask.parallel(1, max_m, (from, to) -> {
            long count = 0;
            for (long m = from|1; m <= to; m += 2) {
                for (long n = 1; n < m; n += 2) {
                    long s = m * (m + n);
                    if (s > limit)
                        break;
                    if (isCoprime(m, n))
                        count += limit / s;
                }
            }
            return count;
        });
    }

    private static long triangle60(long limit) {
        int max_m = (int)((sqrt(24*limit + 9) - 1) / 4);
        return LongRangedTask.parallel(1, max_m, (from, to) -> {
            long count = 0;
            for (long m = from; m <= to; m++) {
                for (long n = 1; n <= m / 2; n++) {
                    long s = (m + n) * (m + m - n);
                    if (s > 3 * limit)
                        break;
                    if (isCoprime(m, n)) {
                        if ((m + n) % 3 == 0)
                            s /= 3;
                        if (s <= limit)
                            count += limit / s;
                    }
                }
            }
            return count;
        });
    }

    private static long triangle120(long limit) {
        int max_m = (int)((sqrt(8*limit + 1) - 3) / 4);
        return LongRangedTask.parallel(1, max_m, (from, to) -> {
            long count = 0;
            for (long m = from; m <= to; m++) {
                for (long n = 1; n < m; n++) {
                    long s = (m + n) * (m + m + n);
                    if (s > limit)
                        break;
                    if (isCoprime(m, n) && (m - n) % 3 != 0)
                        count += limit / s;
                }
            }
            return count;
        });
    }

    public static void main(String[] args) {
        long n = 100_000_000;
        if (args.length > 0)
            n = Long.parseLong(args[0]);
        System.out.println(solve(n));
    }
}
