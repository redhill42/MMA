package euler;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.asin;
import static java.lang.Math.sqrt;
import static java.lang.Math.tan;

public final class Problem607 {
    private Problem607() {}

    @SuppressWarnings("FieldMayBeStatic")
    private static class Solver {
        private final int n;
        private final double a, b;
        private final double[] v;

        public Solver(double A, double B, double[] v) {
            this.n = v.length;
            this.a = (A / sqrt(2) - B) / 2;
            this.b = B / (n - 2);
            this.v = v;
        }

        private double v(int i) {
            return v[i - 1];
        }

        private double k(int i, double x) {
            return (v(i) / v(1)) * (x / sqrt(a*a + x*x));
        }

        private double theta(int i, double x) {
            return asin(k(i, x));
        }

        private double l(int i, double x) {
            if (i == 1) {
                return sqrt(a*a + x*x);
            } else if (i == n) {
                double k = k(i, x);
                return a / sqrt(1 - k*k);
            } else {
                double k = k(i, x);
                return b / sqrt(1 - k*k);
            }
        }

        private double d(int i, double x) {
            if (i == 1) {
                return x;
            } else {
                double k = k(i, x);
                return d(i-1, x) + b * k / sqrt(1 - k*k);
            }
        }

        private double T(double x) {
            double t = 0;
            for (int i = 1; i <= n; i++)
                t += l(i, x) / v(i);
            return t;
        }

        private double y(double x) {
            return (2*a + (n-2)*b - d(n-1, x)) * tan(PI/2 - theta(n, x)) - a;
        }

        private double solve() {
            double e = 1e-10;
            double x1 = a, x2 = 2 * a;
            while (true) {
                double x = (x1 + x2) / 2;
                double y = y(x);
                if (abs(y) < e)
                    return T(x);
                if (y > 0) {
                    x1 = x;
                } else {
                    x2 = x;
                }
            }
        }
    }

    public static double solve(double a, double b, double[] v) {
        return new Solver(a, b, v).solve();
    }

    public static void main(String[] args) {
        double[] v = {10, 9, 8, 7, 6, 5, 10};
        System.out.println(solve(100, 50, v));
    }
}
