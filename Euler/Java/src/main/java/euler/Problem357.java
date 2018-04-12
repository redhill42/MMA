package euler;

import java.util.BitSet;

import euler.util.PrimeSieve;

public class Problem357 {
    private final BitSet primes;

    public Problem357(int limit) {
        this.primes = PrimeSieve.build(limit);
    }

    public long solve() {
        long sum = 0;
        for (int i = 2; i > 0; i = primes.nextSetBit(i+1)) {
            if (check(i-1))
                sum += i-1;
        }
        return sum;
    }

    boolean check(int n) {
        int sq = (int)Math.sqrt(n);
        for (int i = 1; i <= sq; i++) {
            if (n % i == 0 && !primes.get(i + n / i))
                return false;
        }
        return true;
    }

    public static void main(String[] args) {
        Problem357 solver = new Problem357(100_000_000);
        System.out.println(solver.solve());
    }
}