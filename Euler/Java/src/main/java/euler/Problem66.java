package euler;

import java.math.BigInteger;
import euler.algo.PellEquation;
import euler.algo.Rational;

public final class Problem66 {
    private Problem66() {}

    public static int solve(int limit) {
        BigInteger max_x = BigInteger.ZERO;
        int max_d = 0;

        for (int d = 1; d <= limit; d++) {
            Rational r = PellEquation.solve(d, 1);
            if (r != null) {
                BigInteger n = r.numer();
                if (n.compareTo(max_x) > 0) {
                    max_x = n;
                    max_d = d;
                }
            }
        }
        return max_d;
    }

    public static void main(String[] args) {
        System.out.println(solve(1000));
    }
}
