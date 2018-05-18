package euler;

import java.math.BigInteger;

public final class Problem169 {
    private Problem169() {}

    public static long solve(BigInteger n) {
        // this algorithm calculate f(n+1) instead of f(n)
        n = n.add(BigInteger.ONE);

        long a = 1, b = 0;
        while (n.signum() > 0) {
            if (n.testBit(0))
                b += a;
            else
                a += b;
            n = n.shiftRight(1);
        }
        return b;
    }

    public static void main(String[] args) {
        System.out.println(solve(BigInteger.TEN.pow(25)));
    }
}
