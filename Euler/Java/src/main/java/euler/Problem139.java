package euler;

import static euler.algo.Library.isCoprime;
import static euler.algo.Library.isqrt;
import static java.lang.Math.min;

public final class Problem139 {
    private Problem139() {}

    public static int solve(int limit) {
        int count = 0;
        int max_m = isqrt(limit / 2);
        for (int m = 2; m <= max_m; m++) {
            int max_n = min(m - 1, limit / (2 * m) - m);
            for (int n = (m & 1) + 1; n <= max_n; n += 2) {
                if (isCoprime(m, n)) {
                    int a = m * m - n * n;
                    int b = 2 * m * n;
                    int c = m * m + n * n;
                    if (c % (b - a) == 0) {
                        count += limit / (a + b + c);
                    }
                }
            }
        }
        return count;
    }

    public static void main(String[] args) {
        System.out.println(solve(100_000_000));
    }
}
