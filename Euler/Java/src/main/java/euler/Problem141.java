package euler;

import static euler.util.Utils.gcd;
import static euler.util.Utils.isSquare;

public final class Problem141 {
    private Problem141() {}

    public static long solve(long limit) {
        long sum = 0;
        for (long a = 2; a <= 10000; a++) {
            for (long b = 1; b < a; b++) {
                if (gcd(a, b) != 1)
                    continue;
                for (long c = 1; c < limit; c++) {
                    long n = a*a*a*b*c*c + b*b*c;
                    if (n > limit)
                        break;
                    if (isSquare(n))
                        sum += n;
                }
            }
        }
        return sum;
    }

    public static void main(String[] args) {
        System.out.println(solve(1_000_000_000_000L));
    }
}
