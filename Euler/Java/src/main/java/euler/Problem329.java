package euler;

import euler.algo.PrimeSieve;
import euler.algo.Ratio;

import static euler.algo.Library.pow;

public final class Problem329 {
    private Problem329() {}

    private static class Solver {
        private final String sequence;
        private final int limit;
        private final PrimeSieve sieve;

        Solver(String sequence, int limit) {
            this.sequence = sequence;
            this.limit = limit;
            this.sieve = new PrimeSieve(limit);
        }

        public String solve() {
            long p, q;

            // numerator have cases for frog croak in the given sequence
            p = 0;
            for (int square = 1; square <= limit; square++)
                p += jump(square, 0);

            // denominator have cases for frog croak in all possible sequence
            q = limit * pow(6, sequence.length()-1) * 3;

            return Ratio.valueOf(p, q).toString();
        }

        private long jump(int square, int step) {
            // frog croak with probability 1/3 or 2/3
            int croak = 1;
            if (sieve.isPrime(square) ^ (sequence.charAt(step) == 'N'))
                croak = 2;

            if (step == sequence.length() - 1)
                return croak;

            // frog jump one square to the left or right with equal probability
            int left  = square == 1 ? 2 : square - 1;
            int right = square == limit ? limit - 1 : square + 1;
            return croak * (jump(left, step+1) + jump(right, step+1));
        }
    }

    public static String solve(String sequence, int limit) {
        return new Solver(sequence, limit).solve();
    }

    public static void main(String[] args) {
        System.out.println(solve("PPPPNNPPPNPPNPN", 500));
    }
}
