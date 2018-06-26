package euler;

import euler.util.LongRangedTask;

import static euler.algo.Library.isCoprime;
import static euler.algo.Library.isqrt;
import static java.lang.Math.min;

public final class Problem153 {
    private Problem153() {}

    public static long solve(int limit) {
        return sigma(limit) + LongRangedTask.parallel(1, isqrt(limit), (from, to) -> {
            long sum = 0;
            for (long a = from; a <= to; a++) {
                long m = min(a, isqrt(limit - a * a));
                for (long b = 1; b <= m; b++) {
                    if (isCoprime(a, b)) {
                        sum += 2 * (a == b ? a : a + b) * sigma(limit / (a * a + b * b));
                    }
                }
            }
            return sum;
        });
    }

    private static long sigma(long n) {
        long k, m, s = 0;

        m = isqrt(n);
        for (k = 1; k <= m; k++)
            s += k * (n / k);
        for (k = 1; k <= m; k++)
            s += k * (T(n / k) - T(n / (k + 1)));
        if (n / m == m)
            s -= m * m;
        return s;
    }

    private static long T(long n) {
        return n * (n + 1) / 2;
    }

    public static void main(String[] args) {
        System.out.println(solve(100000000));
    }
}
