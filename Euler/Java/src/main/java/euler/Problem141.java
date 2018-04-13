package euler;

import static euler.util.Utils.gcd;
import static euler.util.Utils.isSquare;

public class Problem141 {
    private final long limit;

    public Problem141(long limit) {
        this.limit = limit;
    }

    public long solve() {
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
        Problem141 solver = new Problem141(1_000_000_000_000L);
        System.out.println(solver.solve());
    }
}
