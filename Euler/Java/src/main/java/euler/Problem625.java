package euler;

import euler.algo.TotientSieve;

import static euler.algo.Library.even;
import static euler.algo.Library.isqrt;
import static euler.algo.Library.modmul;
import static euler.algo.TotientSieve.totientSumList;

public final class Problem625 {
    private Problem625() {}

    public static long solve(long n, long m) {
        long sum   = 0;
        int  right = (int)isqrt(n);
        int  left  = (int)(n / (right + 1));

        TotientSieve sieve = new TotientSieve(left);
        for (int k = 1; k <= left; k++) {
            sum += modmul(T(n / k, m), sieve.phi(k), m);
        }

        long[] tsum = totientSumList(n, m, left);
        for (int d = 1; d <= right; d++) {
            sum += modmul(T(d, m), tsum[d] - tsum[d+1], m);
        }

        return sum % m;
    }

    private static long T(long n, long m) {
        return even(n) ? modmul(n >> 1, n + 1, m)
                       : modmul(n, (n + 1) >> 1, m);
    }

    public static void main(String[] args) {
        long n = (long)1e11;
        long m = 998244353;
        if (args.length > 0)
            n = Long.parseLong(args[0]);
        if (args.length > 1)
            m = Long.parseLong(args[1]);
        System.out.println(solve(n, m));
    }
}
