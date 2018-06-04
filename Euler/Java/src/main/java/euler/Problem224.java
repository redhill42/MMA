package euler;

import euler.algo.Pythagorean;

public final class Problem224 {
    private Problem224() {}

    public static long solve(int limit) {
        long[][] start = {{2, 2, 3}};
        return Pythagorean.solve(start, limit, null);
    }

    public static void main(String[] args) {
        int n = 75_000_000;
        if (args.length > 0)
            n = Integer.parseInt(args[0]);
        System.out.println(solve(n));
    }
}
