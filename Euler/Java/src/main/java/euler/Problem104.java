package euler;

import static java.lang.Math.floor;
import static java.lang.Math.log10;
import static java.lang.Math.sqrt;

public final class Problem104 {
    private Problem104() {}

    public static int solve() {
        int a = 1, b = 1;
        for (int n = 1; ; n++) {
            if (isPandigital(a)) {
                double x = n * log10((1 + sqrt(5)) / 2) - log10(sqrt(5));
                int d = (int)Math.pow(10, x - floor(x) + 8);
                if (isPandigital(d))
                    return n;
            }

            int t = (a + b) % 1_000_000_000;
            a = b;
            b = t;
        }
    }

    private static boolean isPandigital(int n) {
        int mask = 0;
        for (int i = 1; i <= 9; i++) {
            int d = n % 10;
            if (d == 0)
                return false;
            if ((mask & (1 << d)) != 0)
                return false;
            mask |= 1 << d;
            n /= 10;
        }
        return n == 0;
    }

    public static void main(String[] args) {
        System.out.println(solve());
    }
}
