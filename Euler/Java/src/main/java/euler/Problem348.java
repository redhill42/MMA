package euler;

import java.util.stream.LongStream;

public class Problem348 {
    private final int count;

    public Problem348(int count) {
        this.count = count;
    }

    public long[] solve() {
        Palindrome p = new Palindrome();
        long[] solutions = new long[count];
        for (int i = 0; i < count; ) {
            long n = p.next();
            int ways = 0;
            long a, b;
            for (a = 1; (b = a*a*a) < n; a++) {
                long sq = (long)Math.sqrt(n - b);
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
        Problem348 solver = new Problem348(5);
        System.out.println(LongStream.of(solver.solve()).sum());
    }
}
