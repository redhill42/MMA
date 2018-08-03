package euler;

public final class Problem147 {
    private Problem147() {}

    public static long solve(long m, long n) {
        if (m < n) return solve(n, m);

        long total = m * (m+1) * (m+2) * n * (n+1) * (n+2) / 36;
        for (long k = 1; k <= n; k++) {
            total += A(k) * (1 + (m - k) + (n - k));
            total += g(k) * (T(m - k) + T(n - k));
        }
        return total;
    }

    private static long T(long n) {
        return n * (n + 1) / 2;
    }

    private static long A(long n) {
        return n * (n - 1) * (4 * n * (n + 1) + 3) / 6;
    }

    private static long g(long n) {
        return n * (4 * n * n - 1) / 3;
    }

    public static void main(String[] args) {
        int m = 47, n = 43;
        if (args.length >= 2) {
            m = Integer.parseInt(args[0]);
            n = Integer.parseInt(args[1]);
        }
        System.out.println(solve(m, n));
    }
}
