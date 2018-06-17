package euler;

import static euler.algo.TotientSieve.totientSum;

public final class Problem72 {
    private Problem72() {}

    public static long solve(int n) {
        return totientSum(n) - 1;
    }

    public static void main(String[] args) {
        int n = 1_000_000;
        if (args.length > 0)
            n = Integer.parseInt(args[0]);
        System.out.println(solve(n));
    }
}
