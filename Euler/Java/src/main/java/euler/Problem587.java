package euler;

import static java.lang.Math.asin;
import static java.lang.Math.sqrt;

public final class Problem587 {
    private Problem587() {}

    public static int solve(double ratio) {
        double L = 1 - Math.PI / 4;
        for (int n = 1; ; n++) {
            if (A(n) < L * ratio)
                return n;
        }
    }

    private static double A(int n) {
        double a = sqrt(2 * n) + 1;
        double b = n + a;
        return ((a + 1) / b - asin(a / b)) / 2;
    }

    public static void main(String[] args) {
        double a = 0.001;
        if (args.length > 0)
            a = Double.parseDouble(args[0]);
        System.out.println(solve(a));
    }
}
