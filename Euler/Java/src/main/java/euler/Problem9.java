package euler;

import static euler.algo.Library.gcd;
import static java.lang.Math.ceil;
import static java.lang.Math.sqrt;
import static java.lang.Math.min;

public final class Problem9 {
    private Problem9() {}

    public static int solve(int s) {
        if ((s & 1) == 1) {
            return -1;
        }

        int s2 = s / 2;
        int max = -1;

        int mlimit = (int)ceil(sqrt(s2)) - 1;
        for (int m = 2; m <= mlimit; m++) {
            if (s2 % m == 0) {
                int sm = s2 / m;
                while (sm % 2 == 0) {
                    sm /= 2;
                }

                int klimit = min(2 * m - 1, sm);
                for (int k = m + (m & 1) + 1; k <= klimit; k += 2) {
                    if (sm % k == 0 && gcd(k, m) == 1) {
                        int n = k - m;
                        int d = s2 / (k * m);
                        int a = d * (m * m - n * n);
                        int b = d * (2 * m * n);
                        int c = d * (m * m + n * n);
                        int abc = a * b * c;
                        if (abc > max)
                            max = abc;
                    }
                }
            }
        }

        return max;
    }

    public static void main(String[] args) {
        int s = 1000;
        if (args.length > 0)
            s = Integer.parseInt(args[0]);
        System.out.println(solve(s));
    }
}
