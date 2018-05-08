package euler;

import java.math.BigInteger;
import euler.algorithms.PellEquation;

public final class Problem66 {
    private Problem66() {}

    public static int solve(int limit) {
        BigInteger max_x = BigInteger.ZERO;
        int max_d = 0;
        BigInteger[] r = new BigInteger[2];

        for (int d = 1; d <= limit; d++) {
            if (PellEquation.solve(d, 1, r)) {
                if (r[0].compareTo(max_x) > 0) {
                    max_x = r[0];
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
