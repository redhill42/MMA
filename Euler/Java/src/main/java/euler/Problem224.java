package euler;

import euler.algo.Pythagorean;
import euler.algo.Triple;

public final class Problem224 {
    private Problem224() {}

    public static long solve(int limit) {
        long[][] start = {{2, 2, 3}};
        long count = 0;
        for (Triple ignored : Pythagorean.solve(start, t -> t.perimeter() <= limit, null))
            count++;
        return count;
    }

    public static void main(String[] args) {
        int n = 75_000_000;
        if (args.length > 0)
            n = Integer.parseInt(args[0]);
        System.out.println(solve(n));
    }
}
