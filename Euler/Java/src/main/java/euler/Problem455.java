package euler;

import java.util.concurrent.ForkJoinPool;
import euler.util.RangedTask;
import static euler.algo.Library.modpow;

public final class Problem455 {
    private Problem455() {}

    @SuppressWarnings("serial")
    private static class SolveTask extends RangedTask<Long> {
        private final int digits;
        private final int[] seed = new int[100];

        SolveTask(int from, int to, int digits) {
            super(from, to);
            this.digits = digits;

            seed[0] = 0; seed[1] = 1;
            for (int n = 2; n < 100; n ++) {
                if (n % 10 == 0)
                    continue;
                int x = 1, y;
                while ((y = modpow(n, x, 100)) != x)
                    x = y;
                seed[n] = x;
            }
        }

        @Override
        protected Long compute(int from, int to) {
            long sum = 0;
            for (int n = from; n <= to; n++)
                sum += f(n);
            return sum;
        }

        /**
         * <pre>
         * For integer n which is not a multiple of 10, find the solution x for
         *     n<sup>x</sup> ≡ x (mod 100),
         * Reapeat x := n<sup>x</sup> mod 10<sup>k</sup>, 2 ≤ k ≤ 9
         * </pre>
         */
        private int f(int n) {
            if (n % 10 == 0)
                return 0;

            int x = seed[n % 100];
            int m = 100;
            for (int k = 2; k <= digits; k++, m *= 10)
                x = modpow(n, x, m);
            return x;
        }

        @Override
        protected Long combine(Long v1, Long v2) {
            return v1 + v2;
        }

        @Override
        protected RangedTask<Long> fork(int from, int to) {
            return new SolveTask(from, to, digits);
        }
    }

    public static long solve(int n, int d) {
        ForkJoinPool pool = new ForkJoinPool();
        long result = pool.invoke(new SolveTask(2, n, d));
        pool.shutdown();
        return result;
    }

    public static void main(String[] args) {
        int n = 1_000_000;
        if (args.length > 0)
            n = Integer.parseInt(args[0]);
        System.out.println(solve(n, 9));
    }
}
