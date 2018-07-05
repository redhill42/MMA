package euler;

import java.math.BigInteger;

import euler.algo.ContinuedFraction;
import euler.algo.Rational;

import static euler.algo.Library.digitSum;

public final class Problem65 {
    private Problem65() {}

    public static BigInteger solve(int iterations) {
        return ContinuedFraction.E.convergents(Rational.class).get(iterations).numer();
    }

    public static void main(String[] args) {
        System.out.println(digitSum(solve(100)));
    }
}
