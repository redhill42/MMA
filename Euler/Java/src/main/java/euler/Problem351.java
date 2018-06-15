package euler;

import static euler.algo.TotientSieve.totientSum;

public final class Problem351 {
    private Problem351() {}

    public static long solve(int n) {
        return 3L*n*(n + 1) - 6*totientSum(n);
    }

    public static void main(String[] args) {
        int n = 100_000_000;
        if (args.length > 0)
            n = Integer.valueOf(args[0]);
        System.out.println(solve(n));
    }
}
