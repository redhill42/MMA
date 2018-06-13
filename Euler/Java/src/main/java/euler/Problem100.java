package euler;

import euler.algo.Pair;
import euler.algo.PellEquation;

public final class Problem100 {
    private Problem100() {}

    public static long solve(long limit) {
        for (Pair p : PellEquation.series(2, 1)) {
            long a = p.y / 2;
            long b = (p.x + p.y + 1) / 2;
            if (a + b >= limit)
                return b;
        }
        return -1;
    }

    public static void main(String[] args) {
        System.out.println(solve(1_000_000_000_000L));
    }
}
