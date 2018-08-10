package euler;

import java.util.function.DoubleUnaryOperator;

import static java.lang.Math.abs;
import static java.lang.Math.acos;
import static java.lang.Math.sqrt;

public final class Problem226 {
    private Problem226() {}

    private static final double EPS = 1e-10;

    private static double blanc(double x) {
        double y = 0, t;
        long div = 1;

        for (;;) {
            if (x > 0.5)
                x = 1 - x;
            if ((t = x / div) < EPS)
                break;
            y += t;
            x *= 2;
            div *= 2;
        }
        return y;
    }

    private static double I(double x) {
        return I(x, 1);
    }

    private static double I(double x, long div) {
        if (x > 0.5)
            return 0.5 / div - I(1 - x, div);
        double t = x * x / (2 * div);
        if (t < EPS)
            return 0;
        return t + I(2 * x, 4 * div);
    }

    private static double findRoot(DoubleUnaryOperator f, double a, double b) {
        while (a < b) {
            double x = (a + b) / 2;
            double y = f.applyAsDouble(x);
            if (abs(y) < EPS)
                return x;
            if (y > 0)
                a = x;
            else
                b = x;
        }
        throw new IllegalStateException("not found");
    }

    private static double circularArea(double x) {
        return 0.25 - x/2 - (1-4*x)*sqrt(x/2-x*x)/8 - acos(sqrt(2*x))/16;
    }

    public static double solve() {
        double ix = findRoot(x -> 0.5 - sqrt(x/2-x*x) - blanc(x), 0, 0.5);
        return I(0.5) - I(ix) - circularArea(ix);
    }

    public static void main(String[] args) {
        System.out.printf("%.8f%n", solve());
    }
}
