package euler;

import java.math.BigInteger;

import static euler.algo.Library.digitSum;

public final class Problem16 {
    private Problem16() {}

    public static int solve(int n) {
        BigInteger power = BigInteger.ONE.shiftLeft(n);
        return digitSum(power);
    }

    public static void main(String[] args) {
        System.out.println(solve(1000));
    }
}
