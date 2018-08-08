package euler;

import euler.util.LongRangedTask;
import static euler.algo.Library.isqrt;

public final class Problem210 {
    private Problem210() {}

    public static long solve(long r) {
        if (r % 8 != 0)
            throw new UnsupportedOperationException("Unsolvable with current algorithm");

        long n = r * r / 32 - 1;
        long u = isqrt(n);
        long ans = 4 * LongRangedTask.parallel(1, (int)u, (from, to) -> {
            long sum = 0;
            for (long a = from; a <= to; a++) {
                long b = n / a;
                sum += psi(b);
                if ((a & 3) == 1)
                    sum += b;
                if ((a & 3) == 3)
                    sum -= b;
            }
            return sum;
        });

        ans -= 4 * u * psi(u);
        ans -= r / 4 - 2;
        ans += 3 * r * r / 2;

        return ans;
    }

    private static int psi(long n) {
        return ((n & 3) == 1 || (n & 3) == 2) ? 1 : 0;
    }

    public static void main(String[] args) {
        int n = 1_000_000_000;
        if (args.length > 0)
            n = Integer.parseInt(args[0]);
        System.out.println(solve(n));
    }
}
