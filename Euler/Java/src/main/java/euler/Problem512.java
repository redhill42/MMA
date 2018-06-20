package euler;

import euler.algo.Sublinear;

public final class Problem512 {
    private Problem512() {}

    public static long solve(int n) {
        Sublinear.Result tsum = Sublinear.totientSumList(n);
        long count = tsum.get(n);
        while (n > 0)
            count -= tsum.get(n >>= 1);
        return count;
    }

    public static void main(String[] args) {
        int n = 500_000_000;
        if (args.length > 0)
            n = Integer.valueOf(args[0]);
        System.out.println(solve(n));
    }
}
