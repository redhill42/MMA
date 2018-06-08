package euler;

import java.math.BigInteger;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;

public final class Problem191 {
    private Problem191() {}

    private static class Solver {
        private final int days;
        private final BigInteger[][][] memo;

        Solver(int days) {
            this.days = days;
            this.memo = new BigInteger[days+1][3][2];
        }

        BigInteger solve() {
            return search(days, 0, 0);
        }

        private BigInteger search(int day, int absent, int late) {
            // the child has 3 ore more consecutive absent day
            if (absent >= 3)
                return ZERO;

            // the child has late on more than one occasion
            if (late > 1)
                return ZERO;

            // reward the child with good attendance
            if (day == 0)
                return ONE;

            if (memo[day][absent][late] != null)
                return memo[day][absent][late];

            BigInteger count = ZERO;
            // assume child neither late or absent today
            count = count.add(search(day - 1, 0, late));
            // assume child is absent today
            count = count.add(search(day - 1, absent + 1, late));
            // assume child is late today
            count = count.add(search(day - 1, 0, late + 1));

            memo[day][absent][late] = count;
            return count;
        }
    }

    public static BigInteger solve(int days) {
        return new Solver(days).solve();
    }

    public static void main(String[] args) {
        int days = 30;
        if (args.length > 0)
            days = Integer.parseInt(args[0]);
        System.out.println(solve(days));
    }
}
