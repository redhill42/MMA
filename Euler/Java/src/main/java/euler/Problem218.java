package euler;

import euler.util.Pythagorean;
import static euler.util.Utils.isSquare;
import static euler.util.Utils.isqrt;

public final class Problem218 {
    private Problem218() {}

    public static long solve(long limit) {
        return Pythagorean.<Long>withHypotenuse(isqrt(limit), 0L, (z, a, b, c) ->
            z + (isSquare(c) ? 0 : perfect(a, b, limit)));
    }

    private static long perfect(long a, long b, long limit) {
        long count = 0;
        while (a <= limit / a && b <= limit / b) {
            long x = Math.abs(a * a - b * b);
            long y = 2 * a * b;
            long z = a * a + b * b;
            if (z > limit)
                break;
            count++;
            a = x; b = y;
        }
        return count;
    }

    public static void main(String[] args) {
        long n = 10_000_000_000_000_000L;
        if (args.length > 0)
            n = Integer.parseInt(args[0]);
        System.out.println(solve(n));
    }
}
