package euler;

import euler.algo.PellEquation;

public final class Problem94 {
    private Problem94() {}

    public static long solve(long limit) {
        long[] sum = new long[1];
        return PellEquation.series(3, 1, (x, y) -> {
            long s = (2*x+1) % 3 == 0 ? 2*x+2 : 2*x-2;
            if (s > limit)
                return sum[0];
            if (s > 2)
                sum[0] += s;
            return null;
        });
    }

    public static void main(String[] args) {
        long limit = 1_000_000_000;
        if (args.length > 0)
            limit = Long.parseLong(args[0]);
        System.out.println(solve(limit));
    }
}
