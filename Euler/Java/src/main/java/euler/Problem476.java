package euler;

import euler.util.RangedTask;

import static java.lang.Math.max;
import static java.lang.Math.sqrt;

public final class Problem476 {
    private Problem476() {}

    @SuppressWarnings("serial")
    private static class SolveTask extends RangedTask<Double> {
        final int n;

        SolveTask(int n, int from, int to) {
            super(from, to, 10);
            this.n = n;
        }

        @Override
        protected Double compute(int from, int to) {
            double sum = 0;
            for (int a = from; a <= to; a++)
            for (int b = a; a + b <= n; b++)
            for (int c = b; c < a + b; c++)
                sum += R(a, b, c);
            return sum;
        }

        private static double R(int a, int b, int c) {
            // take the first circle as the incircle
            double r1 = 0.5 * sqrt(((a+b-c) * (a+c-b) * (b+c-a)) / (double)(a+b+c));

            // take the second circle from corner A
            double r2 = r1 * IX(a, b, c);

            // take the third circle from corner A or corner B
            double r3 = max(r1 * IX(b, a, c), r2 * r2 / r1);

            return Math.PI * (r1 * r1 + r2 * r2 + r3 * r3);
        }

        private static double IX(long a, long b, long c) {
            long t = (a + b - c) * (a + c - b);
            return (4*b*c + t - 4*sqrt(b*c*t)) / (4*b*c - t);
        }

        @Override
        protected Double combine(Double v1, Double v2) {
            return v1 + v2;
        }

        @Override
        protected RangedTask<Double> fork(int from, int to) {
            return new SolveTask(n, from, to);
        }
    }

    public static double solve(int n) {
        double sum = new SolveTask(n, 1, n / 2).invoke();

        long k = n / 2;
        long count = k * (k + 1) * (3*n - 4*k + 1) / 6;
        return sum / count;
    }

    public static void main(String[] args) {
        int n = 1803;
        if (args.length > 0)
            n = Integer.parseInt(args[0]);
        System.out.printf("%.5f%n", solve(n));
    }
}
