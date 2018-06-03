package euler;

import static euler.algo.Library.mod;
import static euler.algo.Library.modmul;
import static java.lang.Math.sqrt;

public final class Problem374 {
    private Problem374() {}

    public static long solve(long N, long M) {
        long m = (long)((sqrt(8 * N + 1) - 1) / 2);
        long l = N - m * (m + 1) / 2;
        long h = 1, f = 1, r = 3;
        long n;

        for (n = 2; n <= m; n++) {
            h = (n * h + f) % M;    // h(n) = n*h(n-1) + (n-1)!
            f = f * n % M;          // n! = n*(n-1)!
            r += modmul((n - 1) * (n + 1), h, M);
            r += modmul(n * (n + 3) / 2, f, M);
        }

        if (l < m)
            r -= modmul(m * (m + 1), f, M);
        if (l < m - 1)
            r -= modmul((m - 1) * (m + 2) / 2, f, M);
        if (l < m - 2) {
            h = 1;
            f = 1;
            for (n = 2; n < m - l; n++) {
                h = (n * h + f) % M;
                f = f * n % M;
            }
            for (; n <= m; n++) {
                h = h * n % M;
                f = f * n % M;
            }
            r -= modmul((m - 1) * (m + 1), h - f, M);
        }

        return mod(r, M);
    }

    public static void main(String[] args) {
        System.out.println(solve((long)1e14, 982451653));
    }
}
