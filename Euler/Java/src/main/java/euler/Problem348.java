package euler;

import java.util.stream.LongStream;
import euler.util.Palindrome;
import static euler.util.Utils.isqrt;

public final class Problem348 {
    private Problem348() {}

    public static long[] solve(int count) {
        Palindrome p = new Palindrome();
        long[] solutions = new long[count];
        for (int i = 0; i < count; ) {
            long n = p.next();
            int ways = 0;
            long a, b;
            for (a = 1; (b = a*a*a) < n; a++) {
                long sq = isqrt(n - b);
                if (sq * sq == n - b)
                    ways++;
            }
            if (ways == 4) {
                solutions[i++] = n;
            }
        }
        return solutions;
    }

    public static void main(String[] args) {
        System.out.println(LongStream.of(solve(5)).sum());
    }
}
