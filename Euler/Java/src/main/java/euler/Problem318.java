package euler;

import static java.lang.Math.ceil;
import static java.lang.Math.log10;
import static java.lang.Math.sqrt;

public final class Problem318 {
    private Problem318() {}

    public static long solve(int n) {
        long sum = 0;
        for (int p = 1; p <= n / 2; p++) {
            for (int q = p + 1; p + q <= n; q++) {
                double x = sqrt(q) - sqrt(p);
                if (x >= 1.0)
                    break;
                sum += (long)(ceil(n / (-2 * log10(x))));
            }
        }
        return sum;
    }

    public static void main(String[] args) {
        System.out.println(solve(2011));
    }
}
