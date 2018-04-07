package euler;

import java.util.BitSet;

public class Problem357 {
    static final int LIMIT = 100000000;
    BitSet primes = new BitSet(LIMIT);

    public Problem357() {
        primes.set(2);
        for (int i = 3; i <= LIMIT; i += 2)
            primes.set(i);

        int sq = (int)Math.sqrt(LIMIT);
        for (int i = 3; i <= sq; i += 2) {
            if (primes.get(i))
                for (int j = i*i; j <= LIMIT; j += i)
                    primes.clear(j);
        }
    }

    boolean check(int n) {
        int sq = (int)Math.sqrt(n);
        for (int i = 1; i <= sq; i++) {
            if (n % i == 0 && !primes.get(i + n / i))
                return false;
        }
        return true;
    }

    public long solve() {
        long sum = 0;
        for (int i = 2; i > 0; i = primes.nextSetBit(i+1)) {
            if (check(i-1))
                sum += i-1;
        }
        return sum;
    }

    public static void main(String[] args) {
        System.out.println(new Problem357().solve());
    }
}