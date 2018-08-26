package euler;

import java.util.Arrays;

public final class Problem623 {
    private Problem623() {}

    private static class Solver {
        private int[][] memo;

        long solve(int n, int m) {
            memo = new int[n + 1][n / 5 + 1];
            for (int[] row : memo)
                Arrays.fill(row, -1);

            long res = 0;
            for (int i = 6; i <= n; i++)
                res += lambda(i, 0, m);
            return res % m;
        }

        long lambda(int n, int k, int m) {
            if (n <= 0)
                return 0;
            if (n == 1)
                return k;

            if (memo[n][k] != -1)
                return memo[n][k];

            long res = 0;
            int i, j;

            for (i = 1; ; i++) {
                j = n - i - 2;
                if (i >= j)
                    break;
                res += lambda(i, k, m) * lambda(j, k, m) % m;
            }

            res <<= 1;
            if (i == j)
                res += lambda(i, k, m) * lambda(j, k, m) % m;
            res += lambda(n - 5, k + 1, m);

            return memo[n][k] = (int)(res % m);
        }
    }

    public static long solve(int n, int m) {
        return new Solver().solve(n, m);
    }

    public static void main(String[] args) {
        int n = 2000;
        if (args.length > 0)
            n = Integer.parseInt(args[0]);
        System.out.println(solve(n, 1_000_000_007));
    }
}
