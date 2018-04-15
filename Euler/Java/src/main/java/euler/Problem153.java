package euler;

import static euler.util.Utils.gcd;
import static java.lang.Math.min;
import static java.lang.Math.sqrt;

public class Problem153 {
    private final int limit;

    public Problem153(int limit) {
        this.limit = limit;
    }

    private static long T(long n) {
        return n * (n + 1) / 2;
    }

    private static long sigma(long n) {
        long k, m, s = 0;

        m = (long)sqrt(n);
        for (k = 1; k <= m; k++)
            s += k * (n / k);
        for (k = 1; k <= m; k++)
            s += k * (T(n / k) - T(n / (k + 1)));
        if (n / m == m)
            s -= m * m;
        return s;
    }

    public long solve() {
        long s = sigma(limit);
        for (long a = 1; a * a <= limit; a++) {
            long m = min(a, (long)sqrt(limit - a * a));
            for (long b = 1; b <= m; b++) {
                if (gcd(a, b) == 1) {
                    s += 2 * (a == b ? a : a + b) * sigma(limit / (a * a + b * b));
                }
            }
        }
        return s;
    }

    public static void main(String[] args) {
        System.out.println(new Problem153(100000000).solve());
    }
}
