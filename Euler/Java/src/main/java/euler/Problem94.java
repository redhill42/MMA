package euler;

import euler.algo.Pair;
import euler.algo.PellEquation;

public final class Problem94 {
    private Problem94() {}

    public static long solve(long limit) {
        long sum = 0;
        for (Pair p : PellEquation.series(3, 1)) {
            long s = (2*p.x+1) % 3 == 0 ? 2*p.x+2 : 2*p.x-2;
            if (s > limit)
                break;
            if (s > 2)
                sum += s;
        }
        return sum;
    }

    public static void main(String[] args) {
        long limit = 1_000_000_000;
        if (args.length > 0)
            limit = Long.parseLong(args[0]);
        System.out.println(solve(limit));
    }
}
