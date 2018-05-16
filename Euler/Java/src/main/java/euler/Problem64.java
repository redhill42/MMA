package euler;

import static euler.algo.Library.isqrt;
import static euler.algo.Library.odd;

public final class Problem64 {
    private Problem64() {}

    public static int solve(int limit) {
        int count = 0;
        for (int n = 2; n <= limit; n++)
            if (odd(period(n)))
                count++;
        return count;
    }

    private static int period(int n) {
        int a0 = isqrt(n);
        if (a0 * a0 == n)
            return 0;

        int a = a0, p = 0, q = 1;
        int k;
        for (k = 0; a != a0 * 2; k++) {
            p = a * q - p;
            q = (n - p * p) / q;
            a = (a0 + p) / q;
        }
        return k;
    }

    public static void main(String[] args) {
        int limit = 10000;
        if (args.length > 0)
            limit = Integer.parseInt(args[0]);
        System.out.println(solve(limit));
    }
}
