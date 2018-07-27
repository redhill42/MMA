package euler;

import static euler.algo.Library.odd;

public final class Problem561 {
    private Problem561() {}

    public static long solve(long m, long n) {
        if (!odd(m))
            throw new UnsupportedOperationException("Unsolvable with current algorithm");

        long s = 0, k = 4;
        while (k <= n) {
            s += n / k;
            k <<= 1;
        }
        return (m + 1) * s;
    }

    public static void main(String[] args) {
        System.out.println(solve(904961, (long)1e12));
    }
}
