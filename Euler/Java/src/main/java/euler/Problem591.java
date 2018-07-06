package euler;

import euler.algo.ContinuedFraction;
import euler.algo.Ratio;
import euler.algo.Rational;

import static euler.algo.Library.isSquare;
import static java.lang.Math.abs;

public final class Problem591 {
    private Problem591() {}

    private static long BQA(int d, Rational beta, long limit) {
        // create high precision number for square root of d
        Rational alpha = ContinuedFraction.sqrt(d).convergents(Rational.class).get(50);

        long[] a = new long[20];
        long[] b = new long[20];
        long p, q;

        a[0] = beta.longValue();
        b[0] = 0;

        Rational eps = beta.subtract(Rational.valueOf(beta.longValue())).abs();
        Rational e;

        int k = 0;
        for (Ratio r : ContinuedFraction.sqrt(d).convergents()) {
            if (r.denom() > limit)
                break;

            for (int i = 0; i <= k; i++) {
                p = a[i] + r.numer();
                q = b[i] - r.denom();
                e = Rational.valueOf(p)
                            .add(Rational.valueOf(q).multiply(alpha))
                            .subtract(beta)
                            .abs();
                if (e.compareTo(eps) < 0 && abs(p) <= limit) {
                    k++;
                    a[k] = p;
                    b[k] = q;
                    eps = e;
                }

                p = a[i] - r.numer();
                q = b[i] + r.denom();
                e = Rational.valueOf(p)
                            .add(Rational.valueOf(q).multiply(alpha))
                            .subtract(beta)
                            .abs();
                if (e.compareTo(eps) < 0 && abs(p) <= limit) {
                    k++;
                    a[k] = p;
                    b[k] = q;
                    eps = e;
                }
            }

            if (k > 1) {
                a[0] = a[k-1]; a[1] = a[k];
                b[0] = b[k-1]; b[1] = b[k];
                k = 1;
            }
        }

        return a[k];
    }

    public static long solve(int n, long limit) {
        Rational x = ContinuedFraction.PI.convergents(Rational.class).get(50);

        long sum = 0;
        for (int d = 2; d <= n; d++) {
            if (isSquare(d))
                continue;
            sum += abs(BQA(d, x, limit));
        }
        return sum;
    }

    public static void main(String[] args) {
        System.out.println(solve(100, (long)1e13));
    }
}
