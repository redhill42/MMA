package euler;

import euler.util.LongRangedTask;
import static euler.algo.Library.modpow;

public final class Problem455 {
    private Problem455() {}

    private static class Solver {
        private final int digits;
        private final int[] seed;

        Solver(int digits) {
            this.digits = digits;

            seed = new int[100];
            seed[0] = 0; seed[1] = 1;
            for (int n = 2; n < 100; n++) {
                if (n % 10 == 0)
                    continue;
                int x = 1, y;
                while ((y = modpow(n, x, 100)) != x)
                    x = y;
                seed[n] = x;
            }
        }

        public long solve(int n) {
            return LongRangedTask.parallel(2, n, (from, to) -> {
                long sum = 0;
                for (int i = from; i <= to; i++)
                    sum += f(i);
                return sum;
            });
        }

        /**
         * <pre>
         * For integer n which is not a multiple of 10, find the solution x for
         *     n<sup>x</sup> ≡ x (mod 100),
         * Repeat x := n<sup>x</sup> mod 10<sup>k</sup>, 2 ≤ k ≤ 9
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
    }

    public static long solve(int n, int d) {
        return new Solver(d).solve(n);
    }

    public static void main(String[] args) {
        int n = 1_000_000;
        if (args.length > 0)
            n = Integer.parseInt(args[0]);
        System.out.println(solve(n, 9));
    }
}
