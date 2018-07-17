package euler;

import euler.util.LongRangedTask;

import static euler.algo.Library.isCoprime;
import static java.lang.Math.sqrt;

public final class Problem195 {
    private Problem195() {}

    public static long solve(int limit) {
        int max_r = (int)(2 * sqrt(3) * limit + 1);

        // m start from 3 to exclude (1,1,1)
        return LongRangedTask.parallel(3, max_r, (from, to) -> {
            int count = 0;
            for (int m = from; m <= to; m++) {
                for (int n = 1; n <= m / 2 && n * (m - n) <= max_r; n++) {
                    if (isCoprime(m, n)) {
                        double r = sqrt(3) / 2 * n * (m - n);
                        if ((m + n) % 3 == 0) r /= 3;
                        if (r <= limit)
                            count += (int)(limit / r);
                    }
                }
            }
            return count;
        });
    }

    public static void main(String[] args) {
        int n = 1053779;
        if (args.length > 0)
            n = Integer.parseInt(args[0]);
        System.out.println(solve(n));
    }
}
