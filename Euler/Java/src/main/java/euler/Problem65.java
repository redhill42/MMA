package euler;

import java.math.BigInteger;

import static euler.algo.Library.digitSum;
import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;

public final class Problem65 {
    private Problem65() {}

    public static BigInteger solve(int iterations) {
        BigInteger p0 = ONE, q0 = ZERO;
        BigInteger p = BigInteger.valueOf(term(0));
        BigInteger q = ONE;
        BigInteger p1, q1;

        for (int i = 1; i < iterations; i++) {
            BigInteger a = BigInteger.valueOf(term(i));
            p1 = p; q1 = q;
            p = p.multiply(a).add(p0);
            q = q.multiply(a).add(q0);
            p0 = p1; q0 = q1;
        }
        return p;
    }

    private static int term(int i) {
        if (i == 0)
            return 2;
        if (--i % 3 == 1)
            return (i / 3 + 1) * 2;
        return 1;
    }

    public static void main(String[] args) {
        System.out.println(digitSum(solve(100)));
    }
}
