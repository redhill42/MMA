package euler;

import euler.algo.ContinuedFraction;
import euler.algo.Rational;

public final class Problem57 {
    private Problem57() {}

    public static int solve(int n, int limit) {
        int count = 0;
        for (Rational r : ContinuedFraction.sqrt(n).convergents(limit, Rational.class))
            if (r.numer().toString().length() > r.denom().toString().length())
                count++;
        return count;
    }

    public static void main(String[] args) {
        System.out.println(solve(2, 1000));
    }
}
