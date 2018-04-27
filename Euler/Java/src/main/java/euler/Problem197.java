package euler;

import static java.lang.Math.abs;
import static java.lang.Math.floor;
import static java.lang.Math.pow;

public final class Problem197 {
    private Problem197() {}

    private static double f(double x) {
        return floor(pow(2, 30.403243784 - x * x)) / 1e9;
    }

    private static double fixedPoint(double x, double e) {
        while (true) {
            double y = f(f(x));
            if (abs(x - y) < e)
                break;
            x = y;
        }
        return x;
    }

    public static double solve() {
        double u = fixedPoint(-1, 1e-9);
        return u + f(u);
    }

    public static void main(String[] args) {
        System.out.println(solve());
    }
}
