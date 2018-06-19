package euler;

import euler.util.LongRangedTask;
import euler.algo.TotientSieve;
import static euler.algo.Library.chineseRemainder;

public final class Problem531 {
    private Problem531() {}

    public static long solve(int start, int end) {
        TotientSieve sieve = new TotientSieve(end);
        return LongRangedTask.parallel(start, end - 1, (from, to) -> {
            long sum = 0;
            for (int n = from; n <= to; n++) {
                for (int m = n + 1; m < end; m++) {
                    long r = chineseRemainder(sieve.phi(n), n, sieve.phi(m), m);
                    if (r > 0) sum += r;
                }
            }
            return sum;
        });
    }

    public static void main(String[] args) {
        System.out.println(solve(1_000_000, 1_005_000));
    }
}
