package euler;

import static java.lang.Math.sqrt;

public final class Problem199 {
    private Problem199() {}

    // Recursively compute the area of tangent circles
    private static double compute(double k1, double k2, double k3, int iteration) {
        // http://mathworld.wolfram.com/DescartesCircleTheorem.html
        double k4 = k1 + k2 + k3 + 2 * sqrt(k1*k2 + k2*k3 + k1*k3);

        double r4 = 1 / k4;
        double area = r4 * r4;

        if (iteration == 1)
            return area;

        return area + compute(k1, k2, k4, iteration - 1)
                    + compute(k2, k3, k4, iteration - 1)
                    + compute(k1, k3, k4, iteration - 1);
    }

    public static double solve(int iteration) {
        // Let the outer most circle be unit circle. It's radius and area is 1.
        // By Descartes circle theorem, the first three circle has the radius:
        double r0 = 2 * sqrt(3) - 3;
        double k0 = 1 / r0;
        double area = 3 * r0 * r0;

        // compute area of circles tangent with outer most circle and their children.
        area += 3 * compute(-1, k0, k0, iteration);

        // compute area of circles tangent with three initial circles and their children.
        area += compute(k0, k0, k0, iteration);

        // returns uncovered area
        return 1 - area;
    }

    public static void main(String[] args) {
        int n = 10;
        if (args.length > 0)
            n = Integer.parseInt(args[0]);
        System.out.printf("%.8f%n", solve(n));
    }
}
