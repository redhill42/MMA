package euler;

import java.util.HashMap;
import java.util.Map;

import euler.algo.Pythagorean;

public final class Problem39 {
    private Problem39() {}

    private static class Solver {
        private final int limit;
        private final Map<Integer, Integer> pyth = new HashMap<>();
        private int max_k, max_v;

        Solver(int limit) {
            this.limit = limit;
        }

        private void collect(int p) {
            for (int k = p; k <= limit; k += p) {
                int v = pyth.merge(k, 1, Integer::sum);
                if (v > max_v) {
                    max_v = v;
                    max_k = k;
                }
            }
        }

        public int solve() {
            Pythagorean.<Void>withPerimeter(limit, null, (z, t) -> {
                collect((int)t.perimeter());
                return null;
            });
            return max_k;
        }
    }

    public static int solve(int limit) {
        return new Solver(limit).solve();
    }

    public static void main(String[] args) {
        int n = 1000;
        if (args.length > 0)
            n = Integer.parseInt(args[0]);
        System.out.println(solve(n));
    }
}
