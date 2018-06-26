package euler;

import euler.util.LongRangedTask;

import static euler.algo.Library.icbrt;
import static euler.algo.Library.isCoprime;
import static euler.algo.Library.isSquare;

public final class Problem141 {
    private Problem141() {}

    public static long solve(long limit) {
        return LongRangedTask.parallel(2, icbrt(limit), (from, to) -> {
            long sum = 0;
            for (long a = from; a <= to; a++) {
                for (long b = 1; b < a; b++) {
                    if (!isCoprime(a, b))
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
        });
    }

    public static void main(String[] args) {
        System.out.println(solve(1_000_000_000_000L));
    }
}
