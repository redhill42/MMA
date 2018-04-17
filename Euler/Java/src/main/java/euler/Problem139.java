package euler;

import static euler.util.Utils.gcd;
import static euler.util.Utils.isqrt;
import static java.lang.Math.min;

public class Problem139 {
    private final int limit;

    public Problem139(int limit) {
        this.limit = limit;
    }

    public int solve() {
        int count = 0;
        int max_m = isqrt(limit / 2);
        for (int m = 2; m <= max_m; m++) {
            int max_n = min(m - 1, limit / (2 * m) - m);
            for (int n = (m & 1) + 1; n <= max_n; n += 2) {
                if (gcd(m, n) == 1) {
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
        Problem139 solver = new Problem139(100_000_000);
        System.out.println(solver.solve());
    }
}
