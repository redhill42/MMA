package euler;

import java.util.Arrays;

import static euler.algo.IntegerPartitions.combinations;
import static euler.algo.IntegerPartitions.partitions;

public final class Problem240 {
    private Problem240() {}

    private static class Solver {
        private final int numDice, maxPoint, numTop, topSum;
        private final long[] factorial;
        private final int[] tally;

        Solver(int numDice, int maxPoint, int numTop, int topSum) {
            this.numDice  = numDice;
            this.maxPoint = maxPoint;
            this.numTop   = numTop;
            this.topSum   = topSum;

            factorial = new long[numDice + 1];
            factorial[0] = 1;
            for (int n = 1; n <= numDice; n++)
                factorial[n] = n * factorial[n - 1];
            tally = new int[maxPoint + 1];
        }

        private long counting(int[] top, int[] bottom) {
            Arrays.fill(tally, 0);
            for (int point : top)
                tally[point]++;
            for (int point : bottom)
                tally[point]++;

            long result = factorial[numDice];
            for (int k : tally)
                if (k > 1)
                    result /= factorial[k];
            return result;
        }

        public Long solve() {
            return
                partitions(topSum, numTop, maxPoint, 0L, (s1, top, k1) ->
                combinations(numDice - numTop, top[0], s1, (s2, bottom, k2) ->
                    s2 + counting(top, bottom)));
        }
    }

    public static long solve(int numDice, int maxPoints, int numTop, int topSum) {
        Solver solver = new Solver(numDice, maxPoints, numTop, topSum);
        return solver.solve();
    }

    public static void main(String[] args) {
        System.out.println(solve(20, 12, 10, 70));
    }
}
