package euler;

import java.util.function.Predicate;

import euler.algo.Pythagorean;
import euler.algo.Triple;
import euler.util.LongRangedTask;

public final class Problem223 {
    private Problem223() {}

    public static long solve(int limit) {
        return LongRangedTask.parallel(1, (limit - 1) / 2, 20, (from, to) -> {
            long total = 0;
            for (int n = from; n <= to; n++)
                total += search(n, limit);
            return total;
        });
    }

    private static long search(int n, int limit) {
        long[][] start = {{1, n, n}};
        Predicate<Triple> filter = t -> {
            if (t.perimeter() > limit)
                return false;
            if (t.a == 1 && t.b != n)
                return false;
            return true;
        };

        long count = 0;
        for (Triple ignored : Pythagorean.solve(start, filter, null))
            count++;
        return count;
    }

    public static void main(String[] args) {
        int n = 25_000_000;
        if (args.length > 0)
            n = Integer.parseInt(args[0]);
        System.out.println(solve(n));
    }
}
