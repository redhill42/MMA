package euler;

import static euler.util.Utils.gcd;
import static java.lang.Math.ceil;
import static java.lang.Math.floor;
import static java.lang.Math.log;

public final class Problem183 {
    private Problem183() {}

    public static long solve(int from, int to) {
        long sum = 0;
        for (int n = from; n <= to; n++)
            sum += D(n);
        return sum;
    }

    private static int M(int n) {
        int k1 = (int)floor(n / Math.E);
        int k2 = (int)ceil(n / Math.E);
        double p1 = k1 * (log(n) - log(k1));
        double p2 = k2 * (log(n) - log(k2));
        return p1 > p2 ? k1 : k2;
    }

    private static int D(int n) {
        int m = M(n); m /= gcd(m, n);
        while ((m & 1) == 0)
            m >>= 1;
        while ((m % 5) == 0)
            m /= 5;
        return m != 1 ? n : -n;
    }

    public static void main(String[] args) {
        System.out.println(solve(5, 10000));
    }
}
