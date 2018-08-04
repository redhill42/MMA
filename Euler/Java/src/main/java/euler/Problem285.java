package euler;

import static java.lang.Math.atan;
import static java.lang.Math.sqrt;

public final class Problem285 {
    private Problem285() {}

    private static double E(long k) {
        double ans, t;

        ans = (sqrt((2*k+1)*(2*k-3)) - sqrt((2*k-1)*(2*k+3))) / (2*k);

        t = (2*k+1)*(2*k+1);
        ans += t / (8*k) * (atan(sqrt(t-4)/2) - atan(2/sqrt(t-4)));

        t = (2*k-1)*(2*k-1);
        ans += t / (8*k) * (acot(sqrt(t-4)/2) - acot(2/sqrt(t-4)));

        return ans;
    }

    private static double acot(double x) {
        return Math.PI/2 - atan(x);
    }

    public static double solve(long n) {
        double ans = (9*acot(4*sqrt(5)) - 4*sqrt(5)) / 8 + 1;
        for (int k = 2; k <= n; k++)
            ans += E(k);
        return ans;
    }

    public static void main(String[] args) {
        long n = 100_000;
        if (args.length > 0)
            n = Long.parseLong(args[0]);
        System.out.printf("%.5f%n", solve(n));
    }
}
