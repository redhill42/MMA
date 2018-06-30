package euler;

import java.math.BigInteger;

import static euler.algo.Library.even;
import static euler.algo.Library.factorize;

public final class Problem359 {
    private Problem359() {}

    public static long solve(long n, long m) {
        BigInteger s = BigInteger.ZERO;
        for (long f : factorize(n).divisors())
            s = s.add(P(f, n / f));
        return s.mod(big(m)).longValue();
    }

    private static BigInteger big(long n) {
        return BigInteger.valueOf(n);
    }

    private static BigInteger P(long f, long r) {
        if (f == 1) {
            return big(r).multiply(big(r + 1)).shiftRight(1);
        }

        if (even(r)) {
            BigInteger a = big(r).multiply(big(r - 1)).shiftRight(1);
            BigInteger b = big(f/2).shiftLeft(1).add(big(r)).multiply(big(f/2)).shiftLeft(1);
            BigInteger c = big(f).pow(2).shiftRight(1);
            return a.add(b).subtract(c);
        } else {
            BigInteger a = big(f/2).shiftLeft(2).add(big(r)).multiply(big(r-1)).shiftRight(1);
            BigInteger b = big(f).pow(2).shiftRight(1);
            return a.add(b);
        }
    }

    public static void main(String[] args) {
        System.out.println(solve(71328803586048L, 100_000_000));
    }
}
