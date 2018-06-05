package euler;

import java.math.BigInteger;

import euler.algo.Rational;
import static euler.algo.Library.choose;
import static java.math.BigInteger.ONE;

public final class Problem323 {
    private Problem323() {}

    public static double solve(int n) {
        Rational value = Rational.ZERO;
        boolean sign = true;
        for (int k = 1; k <= n; k++) {
            BigInteger p2 = ONE.shiftLeft(k);
            Rational term = Rational.valueOf(
                choose(n, k).multiply(p2),
                p2.subtract(ONE));
            value = sign ? value.add(term) : value.subtract(term);
            sign = !sign;
        }
        return value.doubleValue();
    }

    public static void main(String[] args) {
        System.out.printf("%.10f%n", solve(32));
    }
}
