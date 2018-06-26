package euler;

import java.util.BitSet;
import euler.algo.SegmentedSieve;

public final class Problem196 {
    private Problem196() {}

    private static class Solver {
        private final int row;

        private final long offset;
        private final BitSet primes;

        Solver(int row) {
            this.row = row;

            long offset = triangle(1, row - 2);
            int  length = (int)(triangle(1, row + 3) - offset);
            SegmentedSieve sieve = new SegmentedSieve(offset + length);
            SegmentedSieve.Segment segment = sieve.segment(offset);

            this.offset = offset;
            this.primes = new BitSet(length);
            for (long p; (p = segment.next()) > 0; ) {
                primes.set((int)(p - offset));
            }
        }

        /**
         * Sum all members of prime triplets.
         */
        public long solve() {
            long sum = 0;
            for (int col = 1; col <= row; col++)
                if (isMemberOfTriplet(col, row))
                    sum += triangle(col, row);
            return sum;
        }

        private boolean isPrime(long n) {
            return primes.get((int)(n - offset));
        }

        /**
         * Returns the number in the triangle on column x and row y.
         */
        private static long triangle(int x, int y) {
            return (long)y * (y - 1) / 2 + x;
        }

        /**
         * Is (x,y) a member of a prime triplet.
         */
        private boolean isMemberOfTriplet(int x, int y) {
            if (!isPrime(triangle(x, y)))
                return false;

            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    int x2 = x + i, y2 = y + j;
                    if (x2 >= 1 && x2 <= y2 && y2 >= 1)
                        if (isTripletGrid(x2, y2))
                            return true;
                }
            }
            return false;
        }

        /**
         * Is (x,y) the center of a 3x3 prime triplet grid.
         */
        private boolean isTripletGrid(int x, int y) {
            if (!isPrime(triangle(x, y)))
                return false;

            int s = 0;
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    int x2 = x + i, y2 = y + j;
                    if (x2 >= 1 && x2 <= y2 && y2 >= 1)
                        if (isPrime(triangle(x2, y2)) && ++s == 3)
                            return true;
                }
            }
            return false;
        }
    }

    public static long solve(int... input) throws Exception {
        long result = 0;
        for (int n : input) {
            result += new Solver(n).solve();
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        System.out.println(solve(5678027, 7208785));
    }
}
