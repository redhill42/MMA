package euler;

import euler.util.LongRangedTask;

import static euler.algo.Library.even;
import static euler.algo.Library.isqrt;
import static euler.algo.Library.mod;
import static euler.algo.Library.modmul;

public final class Problem401 {
    private Problem401() {}

    public static long solve(long n, long m) {
        long result = LongRangedTask.parallel(1, (int)isqrt(n), (from, to) -> {
            long sqrt_n = isqrt(n);
            long sum = 0;
            for (long a = from; a <= to; a++) {
                long b = n / a;
                sum += P(b, m) - modmul(sqrt_n - b, a * a, m);
            }
            return mod(sum, m);
        });
        return result % m;
    }

    private static long P(long n, long m) {
        long a, b, c;

        if (even(n)) {
            a = n / 2;
            b = n + 1;
            c = 2 * n + 1;
        } else {
            a = n;
            b = (n + 1) / 2;
            c = 2 * n + 1;
        }

        if (a % 3 == 0) {
            a /= 3;
        } else if (b % 3 == 0) {
            b /= 3;
        } else {
            c /= 3;
        }

        return modmul(a, modmul(b, c, m), m);
    }

    public static void main(String[] args) {
        long n = (long)1e15;
        if (args.length > 0)
            n = Long.parseLong(args[0]);
        System.out.println(solve(n, (long)1e9));
    }
}
