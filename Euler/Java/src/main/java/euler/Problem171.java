package euler;

import static euler.util.Utils.isSquare;

public final class Problem171 {
    private Problem171() {}

    public static class Solver {
        private final int length;
        private final long modulo;
        private final long[] factorials;

        public Solver(int length, long modulo) {
            this.length = length;
            this.modulo = modulo;
            this.factorials = new long[length + 1];

            factorials[0] = 1;
            for (int i = 1; i <= length; i++) {
                factorials[i] = factorials[i-1] * i;
            }
        }

        public long solve() {
            int[] digits = new int[length];
            int[] perms = new int[10];
            long totalSum = 0;

            while (next(digits)) {
                int squareSum = 0;
                for (int d : digits) {
                    squareSum += d * d;
                }
                if (isSquare(squareSum)) {
                    for (int i = 0; i <= 9; i++)
                        perms[i] = 0;
                    for (int d : digits)
                        perms[d]++;
                    totalSum = permutationSum(perms, totalSum, modulo);
                }
            }
            return totalSum;
        }

        private static boolean next(int[] digits) {
            int i = 0;
            while (i < digits.length && digits[i] == 9)
                i++;
            if (i == digits.length)
                return false;

            int a = ++digits[i];
            while (--i >= 0)
                digits[i] = a;
            return true;
        }

        private long permutationSum(int[] perms, long sum, long modulo) {
            long f = factorials[length];
            for (int p : perms) {
                f /= factorials[p];
            }

            long a = 0;
            for (int i = 1; i <= 9; i++)
                a += i * perms[i];
            a = f * a / length;

            for (int i = 0; i < length; i++) {
                sum = (sum + a) % modulo;
                a = (a * 10) % modulo;
            }
            return sum;
        }
    }

    public static long solve(int n, int length, long modulo) {
        switch (n) {
        case 1:
            return solve1(length, modulo);
        case 2:
            return solve2(length, modulo);
        default:
            throw new IllegalArgumentException();
        }
    }

    public static long solve1(int length, long modulo) {
        return new Solver(length, modulo).solve();
    }

    public static long solve2(int length, long modulo) {
        int limit = 9 * 9 * length;
        long[] S = new long[limit + 1];
        long[] T = new long[limit + 1];
        long[] E = new long[limit + 1];
        long[] F = new long[limit + 1];
        long pow10 = 1;

        S[0] = 0;
        E[0] = 1;

        for (int n = 1; n <= length; n++) {
            for (int i = 0; i <= 81 * (n - 1); i++) {
                T[i] = S[i];
                F[i] = E[i];
            }
            for (int i = 0; i <= 81 * n; i++) {
                S[i] = 0;
                E[i] = 0;
            }
            for (int i = 0; i <= 81 * (n - 1); i++) {
                for (int d = 0; d < 10; d++) {
                    S[i+d*d] += (T[i] + d * F[i] * pow10) % modulo;
                    E[i+d*d] += F[i];
                }
            }
            for (int i = 0; i <= 81 * n; i++) {
                S[i] %= modulo;
                E[i] %= modulo;
            }
            pow10 = (pow10 * 10) % modulo;
        }

        long sum = 0;
        for (int i = 0; i*i <= 81 * length; i++) {
            sum += S[i*i];
        }
        return sum % modulo;
    }

    public static void main(String[] args) {
        System.out.println(solve2(20, 1_000_000_000));
    }
}
