package euler;

import static euler.algo.Library.isurd;
import static euler.algo.Library.pow;

public final class Problem617 {
    private Problem617() {}

    public static long solve(final long n) {
        int  e, k, i;
        long a, b;
        long z = 0;

        for (e = 2; (1L << e) <= n; e++) {
            a = isurd(n, e);
            while (pow(a, e) + a > n)
                a--;
            for (i = 1; i <= e; i++) {
                k = isurd(e, i);
                if (pow(k, i) == e) {
                    z += (a - 1) * i;
                    b = isurd(a, k);
                    while (b > 1) {
                        z += b - 1;
                        b = isurd(b, k);
                    }
                }
            }
        }

        return z;
    }

    public static void main(String[] args) {
        long n = (long)1e18;
        if (args.length > 0)
            n = Long.parseLong(args[0]);
        System.out.println(solve(n));
    }
}
