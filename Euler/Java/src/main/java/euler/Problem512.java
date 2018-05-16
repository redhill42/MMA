package euler;

import euler.algo.TotientSieve;

public final class Problem512 {
    private Problem512() {}

    public static long solve(int n) {
        long count = TotientSieve.sum(n);
        while (n > 0)
            count -= TotientSieve.sum(n >>= 1);
        return count;
    }

    public static void main(String[] args) {
        int n = 500_000_000;
        if (args.length > 0)
            n = Integer.valueOf(args[0]);
        System.out.println(solve(n));
    }
}
