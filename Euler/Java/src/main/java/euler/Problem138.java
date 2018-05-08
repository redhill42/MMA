package euler;

import euler.algorithms.PellEquation;

public final class Problem138 {
    private Problem138() {}

    private static class Solver implements PellEquation.SeriesFunction<Long> {
        private int count;
        private long sum;

        Solver(int count) {
            this.count = count;
        }

        @Override
        public Long apply(long x, long y) {
            if (x != 2 && x != -2) {
                sum += y;
                if (--count == 0)
                    return sum;
            }
            return null;
        }

        public long solve() {
            return PellEquation.series(5, -1, this);
        }
    }

    public static long solve(int count) {
        Solver solver = new Solver(count);
        return solver.solve();
    }

    public static void main(String[] args) {
        System.out.println(solve(12));
    }
}
