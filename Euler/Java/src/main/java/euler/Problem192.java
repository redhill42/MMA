package euler;

import euler.algo.ContinuedFraction;
import static java.lang.Math.ceil;
import static java.lang.Math.sqrt;

public final class Problem192 {
    private Problem192() {}

    public static long solve(int from, int to, long bound) {
        long sum = 0;
        int sqr = (int)ceil(sqrt(from));
        for (int n = from; n <= to; n++) {
            if (n == sqr * sqr) {
                sqr++;
            } else {
                ContinuedFraction cf = ContinuedFraction.sqrt(n);
                sum += cf.bestApproximation(bound).denom();
            }
        }
        return sum;
    }

    public static void main(String[] args) {
        System.out.println(solve(2, 100000, (long)1e12));
    }
}
