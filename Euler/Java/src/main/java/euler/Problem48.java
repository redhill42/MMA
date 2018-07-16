package euler;

import static euler.algo.Library.modpow;

public final class Problem48 {
    private Problem48() {}

    public static long solve(int n, long m) {
        long sum = 0;
        for (int i = 1; i <= n; i++)
            sum += modpow(i, i, m);
        return sum % m;
    }

    public static void main(String[] args) {
        System.out.println(solve(1000, (long)1e10));
    }
}
