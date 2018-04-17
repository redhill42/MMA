package euler;

import java.util.BitSet;
import java.util.Scanner;

public class Problem10 {
    private final int limit;
    private final BitSet sieve;

    public Problem10(int limit) {
        int sievebound = (limit - 1) / 2;
        int crosslimit = ((int)Math.sqrt(limit) - 1) / 2;

        this.limit = limit;
        this.sieve = new BitSet(sievebound + 1);
        for (int i = 1; i > 0 && i <= crosslimit; i = sieve.nextClearBit(i + 1)) {
            for (int j = 2 * i * (i + 1); j <= sievebound; j += 2 * i + 1)
                sieve.set(j);
        }
    }

    public long solve() {
        return solve(limit);
    }

    public long solve(int n) {
        int sievebound = (n - 1) / 2;
        long sum = 2;
        for (int i = 1; i > 0 && i <= sievebound; i = sieve.nextClearBit(i + 1))
            sum += 2 * i + 1;
        return sum;
    }

    public static void main(String[] args) {
        Problem10 solver = new Problem10(1000000);
        Scanner in = new Scanner(System.in);
        int t = in.nextInt();
        while (--t >= 0) {
            int n = in.nextInt();
            System.out.println(solver.solve(n));
        }
    }
}
