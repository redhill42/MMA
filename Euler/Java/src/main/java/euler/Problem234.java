package euler;

import euler.algo.SegmentedSieve;
import euler.algo.SegmentedSieve.Segment;

public final class Problem234 {
    private Problem234() {}

    public static long solve(long limit) {
        Segment primes = new SegmentedSieve(limit).segment();
        long sum = 0;

        for (long low = primes.next(); low * low <= limit; ) {
            long high = primes.next();
            long from = low * low;
            long to = Math.min(limit + 1, high * high);

            sum += sum(from, to, low);
            sum += sum(from, to, high);
            sum -= sum(from, to, low * high) * 2;
            low = high;
        }
        return sum;
    }

    private static long sum(long from, long to, long divisor) {
        from = from / divisor + 1;
        to   = (to - 1) / divisor;
        return (to - from + 1) * (from + to) / 2 * divisor;
    }

    public static void main(String[] args) {
        System.out.println(solve(15));
        System.out.println(solve(1000));
        System.out.println(solve(999966663333L));
    }
}
