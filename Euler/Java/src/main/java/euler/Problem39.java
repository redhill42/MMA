package euler;

import java.util.HashMap;
import java.util.Map;

import euler.util.Pythagorean;
import euler.util.Pythagorean.TripletFunction;

public final class Problem39 {
    private Problem39() {}

    private static class Solver implements TripletFunction<Void> {
        private final int limit;
        private final Map<Integer, Integer> pyth = new HashMap<>();
        private int max_k, max_v;

        Solver(int limit) {
            this.limit = limit;
        }

        @Override
        public Void compute(Void z, long a, long b, long c) {
            int p = (int)(a + b + c);
            for (int k = p; k <= limit; k += p) {
                int v = pyth.merge(k, 1, Integer::sum);
                if (v > max_v) {
                    max_v = v;
                    max_k = k;
                }
            }
            return null;
        }

        public int solve() {
            Pythagorean.withPerimeter(limit, null, this);
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
