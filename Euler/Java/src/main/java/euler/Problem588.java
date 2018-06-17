package euler;

import static euler.algo.Library.exponent;

public final class Problem588 {
    private Problem588() {}

    public static long solve(int k) {
        long sum = 0;
        long n = 10;
        for (int i = 1; i <= k; i++) {
            sum += Q(n);
            n *= 10;
        }
        return sum;
    }

    private static long Q(long n) {
        switch ((int)(n % 8)) {
        case 1:
            return 5 * Q((n - 1) / 8);
        case 3:
            return 2 * Q((n - 3) / 2 + 1) - 3 * Q((n - 3) / 8);
        case 5:
            return 3 * Q((n - 5) / 4 + 1) + 2 * Q((n - 5) / 8);
        case 7:
            return 2 * Q((n - 7) / 2 + 1) + 3 * Q((n - 7) / 4 + 1) - 6 * Q((n - 7) / 8);
        default:
            return n == 0 ? 1 : Q(n >> exponent(n, 2));
        }
    }

    public static void main(String[] args) {
        System.out.println(solve(18));
    }
}
