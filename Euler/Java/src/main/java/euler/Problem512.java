package euler;

import static euler.algo.TotientSieve.totientSum;

public final class Problem512 {
    private Problem512() {}

    public static long solve(int n) {
        long count = totientSum(n);
        while (n > 0)
            count -= totientSum(n >>= 1);
        return count;
    }

    public static void main(String[] args) {
        int n = 500_000_000;
        if (args.length > 0)
            n = Integer.valueOf(args[0]);
        System.out.println(solve(n));
    }
}
