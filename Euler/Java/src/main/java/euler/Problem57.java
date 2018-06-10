package euler;

import euler.algo.ContinuedFraction;

public final class Problem57 {
    private Problem57() {}

    public static int solve(int n, int limit) {
        int[] count = {0};
        ContinuedFraction.sqrt(n).convergents(limit, r -> {
            if (r.getNumerator().toString().length() >
                r.getDenominator().toString().length())
                count[0]++;
            return true;
        });
        return count[0];
    }

    public static void main(String[] args) {
        System.out.println(solve(2, 1000));
    }
}
