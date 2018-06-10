package euler;

import java.math.BigInteger;

import euler.algo.ContinuedFraction;
import static euler.algo.Library.digitSum;

public final class Problem65 {
    private Problem65() {}

    public static BigInteger solve(int iterations) {
        return ContinuedFraction.E.convergent(iterations).getNumerator();
    }

    public static void main(String[] args) {
        System.out.println(digitSum(solve(100)));
    }
}
