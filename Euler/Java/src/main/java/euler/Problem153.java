package euler;

import static euler.util.Utils.gcd;
import static euler.util.Utils.isqrt;
import static java.lang.Math.min;

public final class Problem153 {
    private Problem153() {}

    private static long T(long n) {
        return n * (n + 1) / 2;
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

    public static long solve(int limit) {
        long s = sigma(limit);
        for (long a = 1; a * a <= limit; a++) {
            long m = min(a, isqrt(limit - a * a));
            for (long b = 1; b <= m; b++) {
                if (gcd(a, b) == 1) {
                    s += 2 * (a == b ? a : a + b) * sigma(limit / (a * a + b * b));
                }
            }
        }
        return s;
    }

    public static void main(String[] args) {
        System.out.println(solve(100000000));
    }
}
