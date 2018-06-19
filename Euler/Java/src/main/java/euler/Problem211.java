package euler;

import euler.algo.FactorizationSieve;
import euler.util.LongRangedTask;

import static euler.algo.Library.isSquare;

public final class Problem211 {
    private Problem211() {}

    public static long solve(int low, int high) {
        FactorizationSieve sieve = new FactorizationSieve(high);
        return LongRangedTask.parallel(low, high, 10000, (from, to) -> {
            long sum = 0;
            for (int n = from; n <= to; n++)
                if (isSquare(sieve.sigma(2, n)))
                    sum += n;
            return sum;
        });
    }

    public static void main(String[] args) {
        System.out.println(solve(1, 64_000_000));
    }
}
