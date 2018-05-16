package euler;

import euler.algo.PellEquation;

public final class Problem100 {
    private Problem100() {}

    public static long solve(long limit) {
        return PellEquation.series(2, 1, (x, y) -> {
            long a = y / 2;
            long b = (x + y + 1) / 2;
            if (a + b >= limit)
                return b;
            return null;
        });
    }

    public static void main(String[] args) {
        System.out.println(solve(1_000_000_000_000L));
    }
}
