package euler;

import java.math.BigInteger;

import static euler.algo.Library.digitSum;

public final class Problem20 {
    private Problem20() {}

    public static int solve(int n) {
        BigInteger factorial = BigInteger.ONE;
        for (int i = 2; i <= n; i++)
            factorial = factorial.multiply(BigInteger.valueOf(i));
        return digitSum(factorial);
    }

    public static void main(String[] args) {
        System.out.println(solve(100));
    }
}
