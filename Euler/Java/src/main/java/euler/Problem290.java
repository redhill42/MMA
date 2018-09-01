package euler;

import java.util.Arrays;

public final class Problem290 {
    private Problem290() {}

    private static class Solver {
        private long[] memo;

        private static final int L = 240; // big enough for K<=18

        long solve(int n, int s) {
            memo = new long[(n + 1) * s * L];
            Arrays.fill(memo, -1);
            return search(n, s, 0, 0);
        }

        private long search(int n, int s, int u, int v) {
            int id = (n * s + u) * L + (v + L/2);
            if (memo[id] != -1)
                return memo[id];

            long result = 0;
            if (n == 1) {
                for (int d = 0; d <= 9; d++) {
                    if (digitSum(s * d + u) + v == d)
                        result++;
                }
            } else {
                for (int d = 0; d <= 9; d++) {
                    int u2 = (s * d + u) / 10;
                    int v2 = (s * d + u) % 10 + v - d;
                    result += search(n - 1, s, u2, v2);
                }
            }
            return memo[id] = result;
        }

        private static int digitSum(long n) {
            int r = 0;
            while (n > 0) {
                r += n % 10;
                n /= 10;
            }
            return r;
        }
    }

    public static long solve(int n, int s) {
        return new Solver().solve(n, s);
    }

    public static void main(String[] args) {
        System.out.println(solve(18, 137));
    }
}
