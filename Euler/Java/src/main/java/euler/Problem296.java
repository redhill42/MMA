package euler;

import euler.util.LongRangedTask;

import static euler.algo.Library.gcd;
import static java.lang.Math.min;

public final class Problem296 {
    private Problem296() {}

    @SuppressWarnings("unused")
    public static long bruteForce(int limit) {
        return LongRangedTask.parallel(2, limit / 3, (from, to) -> {
            long count = 0;
            for (int a = from; a <= to; a++) {
                for (int b = a; b <= (limit - a) / 2; b++) {
                    int d = (a + b) / gcd(a, b);
                    int u = min(a + b - 1, limit - a - b);
                    count += u / d - (b - 1) / d;
                }
            }
            return count;
        });
    }

    public static long solve(int limit) {
        long count = 0;

        int n = limit / 6;
        int a = 1, b = n, a1 = 1, b1 = n - 1;

        while (a <= b) {
            int d = a + b;
            for (int k = d / a; ; k++) {
                int u = min(k - 1, limit / d - k);
                int l = (b * k - 1) / d;
                if (u < l)
                    break;
                count += u - l;
            }

            // generate coprime a and b from Farey sequence
            int k = (n + b) / b1;
            int a2 = k * a1 - a;
            int b2 = k * b1 - b;
            a = a1; b = b1; a1 = a2; b1 = b2;
        }

        return count;
    }

    public static void main(String[] args) {
        int n = 100_000;
        if (args.length > 0)
            n = Integer.parseInt(args[0]);
        System.out.println(solve(n));
    }
}
