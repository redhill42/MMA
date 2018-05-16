package euler;

import java.util.BitSet;

public final class Problem10 {
    private Problem10() {}

    private static class Solver {
        private final BitSet sieve;

        public Solver(int limit) {
            int sievebound = (limit - 1) / 2;
            int crosslimit = ((int)Math.sqrt(limit) - 1) / 2;

            this.sieve = new BitSet(sievebound + 1);
            for (int i = 1; i > 0 && i <= crosslimit; i = sieve.nextClearBit(i + 1)) {
                for (int j = 2 * i * (i + 1); j <= sievebound; j += 2 * i + 1)
                    sieve.set(j);
            }
        }

        public long solve(int n) {
            int sievebound = (n - 1) / 2;
            long sum = 2;
            for (int i = 1; i > 0 && i <= sievebound; i = sieve.nextClearBit(i + 1))
                sum += 2 * i + 1;
            return sum;
        }
    }

    public static long solve(int limit) {
        return new Solver(limit).solve(limit);
    }

    public static void main(String[] args) {
        int limit = 2_000_000;
        if (args.length > 0)
            limit = Integer.parseInt(args[0]);
        System.out.println(solve(limit));
    }
}
