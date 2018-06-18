package euler;

import static euler.algo.Library.isqrt;
import static euler.algo.Library.moebius;

public final class Problem193 {
    private Problem193() {}

    public static long solve(long limit) {
        int maxd = (int)isqrt(limit - 1);
        byte[] mu = moebius(maxd);
        long q = 0;

        for (int d = 1; d <= maxd; d++) {
            if (mu[d] != 0)
                q += mu[d] * (limit - 1) / d / d;
        }
        return q;
    }

    public static void main(String[] args) {
        System.out.println(solve(1L << 50));
    }
}
