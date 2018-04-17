package euler;

import euler.util.PrimeSieve;
import static euler.util.Utils.isqrt;

public class Problem357 {
    private final PrimeSieve sieve;

    public Problem357(int limit) {
        this.sieve = new PrimeSieve(limit);
    }

    public long solve() {
        long sum = 0;
        for (int i = 2; i > 0; i = sieve.nextPrime(i)) {
            if (check(i-1))
                sum += i-1;
        }
        return sum;
    }

    boolean check(int n) {
        for (int i = 1, sq = isqrt(n); i <= sq; i++) {
            if (n % i == 0 && !sieve.isPrime(i + n / i))
                return false;
        }
        return true;
    }

    public static void main(String[] args) {
        Problem357 solver = new Problem357(100_000_000);
        System.out.println(solver.solve());
    }
}