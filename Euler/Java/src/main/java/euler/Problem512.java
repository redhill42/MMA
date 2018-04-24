package euler;

import euler.util.TotientSieve;

public final class Problem512 {
    private Problem512() {}

    public static long solve(int n) {
        TotientSieve sieve = new TotientSieve(n);
        long sum = 0;
        for (int i = 1; i <= n; i += 2)
            sum += sieve.phi(i);
        return sum;
    }

    public static void main(String[] args) {
        System.out.println(solve(500_000_000));
    }
}
