package euler;

import java.util.HashSet;
import java.util.Set;

import euler.algo.Ratio;
import euler.algo.Rational;

import static euler.algo.Library.gcd;
import static euler.algo.Library.isCoprime;
import static euler.algo.Library.isSquare;
import static euler.algo.Library.isqrt;

public final class Problem180 {
    private Problem180() {}

    public static long solve(int k) {
        Rational s = Rational.ZERO;
        for (Ratio r : search(k))
            s = s.add(r.toRational());
        return s.numer().add(s.denom()).longValue();
    }

    private static Set<Ratio> search(int k) {
        Set<Ratio> sol = new HashSet<>();

        for (int b = 2; b <= k; b++) {
            for (int d = 2; d <= k; d++) {
                for (int a = 1; a < b; a++) {
                    if (!isCoprime(a, b))
                        continue;

                    // make sure c/d >= a/b
                    for (int c = (a * d + b - 1) / b; c < d; c++) {
                        if (!isCoprime(c, d))
                            continue;

                        // check for a/b + c/d = p/q
                        int p = a * d + b * c;
                        int q = b * d;
                        sow(sol, a, b, c, d, p, q, k);

                        // check for (a/b)^2 + (c/d)^2 = (p/q)^2
                        p = (a*d) * (a*d) + (b*c) * (b*c);
                        if (isSquare(p)) {
                            sow(sol, a, b, c, d, isqrt(p), q, k);
                        }

                        // check for (a/b)^-1 + (c/d)^-1 = (p/q)^-1
                        // or b/a + d/c = q/p
                        q = b * c + a * d;
                        p = a * c;
                        sow(sol, a, b, c, d, p, q, k);

                        // check for (a/b)^-2 + (c/d)^-2 = (p/q)^-2
                        // or (b/a)^2 + (d/c)^2 = (q/p)^2
                        q = (b*c) * (b*c) + (a*d) * (a*d);
                        if (isSquare(q)) {
                            sow(sol, a, b, c, d, p, isqrt(q), k);
                        }
                    }
                }
            }
        }

        return sol;
    }

    private static void sow(Set<Ratio> sol, int a, int b, int c, int d, int p, int q, int k) {
        int g = gcd(p, q);
        p /= g; q /= g;
        if (p < q && q <= k) {
            sol.add(Ratio.valueOf(a, b).add(Ratio.valueOf(c, d)).add(Ratio.valueOf(p, q)));
        }
    }

    public static void main(String[] args) {
        int n = 35;
        if (args.length > 0)
            n = Integer.parseInt(args[0]);
        System.out.println(solve(n));
    }
}
