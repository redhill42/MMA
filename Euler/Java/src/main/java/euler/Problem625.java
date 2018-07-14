package euler;

import euler.algo.Sublinear;

import static euler.algo.Library.isqrt;
import static euler.algo.Library.modmul;
import static euler.algo.Library.tri;
import static euler.algo.Sublinear.totientModSumList;

public final class Problem625 {
    private Problem625() {}

    public static long solve(long n, long m) {
        Sublinear.TotientSumResult tsum = totientModSumList(n, m);
        long sqrt_n = isqrt(n);
        long T_sqrt_n = tri(sqrt_n, m);
        long sum = 0;

        for (int a = 1; a <= sqrt_n; a++) {
            long b = n / a;
            sum += modmul(a, tsum.get(b), m);
            sum += modmul(tri(b, m) - T_sqrt_n, tsum.term(a), m);
        }
        return sum % m;
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
