package euler;

import java.util.Arrays;

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

        private long counting(int[] dice) {
            Arrays.fill(tally, 0);
            for (int point : dice)
                tally[point]++;

            long result = factorial[numDice];
            for (int k : tally)
                if (k > 1)
                    result /= factorial[k];
            return result;
        }

        private long search(int[] dice, int next) {
            if (next == numDice) {
                return counting(dice);
            }

            if (next == numTop) {
                int sum = 0;
                for (int i = 0; i < next; i++)
                    sum += dice[i];
                if (sum != topSum)
                    return 0;
            }

            long result = 0;
            int point = next == 0 ? maxPoint : dice[next - 1];
            while (point > 0) {
                dice[next] = point;
                result += search(dice, next + 1);
                point--;
            }
            return result;
        }

        public long solve() {
            return search(new int[numDice], 0);
        }
    }

    public static long solve(int numDice, int maxPoints, int numTop, int topSum) {
        Solver solver = new Solver(numDice, maxPoints, numTop, topSum);
        return solver.solve();
    }

    public static void main(String[] args) {
        System.out.println(solve(5, 6, 3, 15));
        System.out.println(solve(20, 12, 10, 70));
    }
}
