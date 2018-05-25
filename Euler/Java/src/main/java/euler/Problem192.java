package euler;

import euler.algo.ContinuedFraction;
import static euler.algo.Library.even;
import static euler.algo.Library.odd;
import static java.lang.Math.ceil;
import static java.lang.Math.sqrt;

public final class Problem192 {
    private Problem192() {}

    public static long solve(int from, int to, long bound) {
        long sum = 0;
        int sqr = (int)ceil(sqrt(from));
        for (int n = from; n <= to; n++) {
            if (n == sqr * sqr) {
                sqr++;
            } else {
                sum += search(n, bound);
            }
        }
        return sum;
    }

    private static long search(final int n, final long bound) {
        ContinuedFraction cf = ContinuedFraction.sqrt(n);

        long p0 = 1;
        long q0 = 0;
        long p1 = cf.term(0);
        long q1 = 1;
        long p, q;

        for (int i = 1; ; i++) {
            int a = cf.term(i);
            p = a * p1 + p0;
            q = a * q1 + q0;

            if (q > bound) {
                long k = (q - bound) / q1 + 1;
                int b = (a + 1) / 2;
                if (even(a) && !allowHalf(cf, i))
                    b++;
                return k > a - b ? q1 : q - k * q1;
            }

            p0 = p1;
            q0 = q1;
            p1 = p;
            q1 = q;
        }
    }

    private static boolean allowHalf(ContinuedFraction cf, int k) {
        int s = 1;
        for (int i = k; i > 0; i--) {
            int diff = s * (cf.term(i) - cf.term(2 * k - i));
            if (diff > 0)
                return true;
            if (diff < 0)
                return false;
            s = -s;
        }
        return odd(k - 1);
    }

    public static void main(String[] args) {
        System.out.println(solve(2, 100000, (long)1e12));
    }
}
