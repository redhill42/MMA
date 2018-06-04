package euler;

import java.util.concurrent.ForkJoinPool;

import euler.algo.Pythagorean;
import euler.util.RangedTask;

public final class Problem223 {
    private Problem223() {}

    @SuppressWarnings("serial")
    private static class SolveTask extends RangedTask<Long> {
        private final int limit;

        SolveTask(int limit, int from, int to) {
            super(from, to, 20);
            this.limit = limit;
        }

        @Override
        protected Long compute(int from, int to) {
            long total = 0;
            for (int n = from; n <= to; n++)
                total += search(n);
            return total;
        }

        private long search(int n) {
            long[][] start = {{1, n, n}};
            return Pythagorean.solve(start, t -> {
                if (t.perimeter() > limit)
                    return false;
                if (t.a == 1 && t.b != n)
                    return false;
                return true;
            }, null);
        }

        @Override
        protected Long combine(Long v1, Long v2) {
            return v1 + v2;
        }

        @Override
        protected RangedTask<Long> fork(int from, int to) {
            return new SolveTask(limit, from, to);
        }
    }

    public static long solve(int limit) {
        ForkJoinPool pool = new ForkJoinPool();
        long result = pool.invoke(new SolveTask(limit, 1, (limit - 1) / 2));
        pool.shutdown();
        return result;
    }

    public static void main(String[] args) {
        int n = 25_000_000;
        if (args.length > 0)
            n = Integer.parseInt(args[0]);
        System.out.println(solve(n));
    }
}
