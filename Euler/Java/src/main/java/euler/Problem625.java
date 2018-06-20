package euler;

import euler.algo.Sublinear;

import static euler.algo.Library.even;
import static euler.algo.Library.isqrt;
import static euler.algo.Library.modmul;
import static euler.algo.Sublinear.totientModSumList;

public final class Problem625 {
    private Problem625() {}

    public static long solve(long n, long m) {
        Sublinear.TotientSumResult tsum = totientModSumList(n, m);
        long sqrt_n = isqrt(n);
        long T_sqrt_n = T(sqrt_n, m);
        long sum = 0;

        for (int a = 1; a <= sqrt_n; a++) {
            long b = n / a;
            sum += modmul(a, tsum.get(b), m);
            sum += modmul(T(b, m) - T_sqrt_n, tsum.term(a), m);
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
