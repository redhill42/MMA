package euler;

import euler.util.LongRangedTask;

import static euler.algo.Library.isqrt;
import static euler.algo.Library.pow;
import static java.lang.Math.log;

public final class Problem346 {
    private Problem346() {}

    public static long solve(long limit) {
        long result = 1 + LongRangedTask.parallel(2, (int)isqrt(limit), (from, to) -> {
            long sum = 0;
            for (int b = from; b <= to; b++) {
                int n = (int)(log(limit * (b - 1) + 1) / log(b)) - 1;
                sum += (n-1)*(b+1) + (pow(b, n) - b*n + n - 1) / (b-1)/(b-1) * b*b;
            }
            return sum;
        });

        if (limit >= 31)
            result -= 31;
        if (limit >= 8191)
            result -= 8191;
        return result;
    }

    public static void main(String[] args) {
        System.out.println(solve(1_000_000_000_000L));
    }
}
