package euler;

import static euler.algo.Library.modpow;
import static euler.algo.Library.pow;

public final class Problem188 {
    private Problem188() {}

    public static long solve(int a, int k, int digits) {
        return tetration(a, k, pow(10, digits));
    }

    private static long tetration(int a, int k, long n) {
        if (k == 0)
            return 1;
        if (n == 1)
            return 0;
        return modpow(a, tetration(a, k - 1, phi(n)), n);
    }

    private static long phi(long n) {
        // Initially n is 10^k = 2^k*5^k, so compute phi(n) is easy as:
        // phi(n) = phi(2^a*5^b) = (2-1)*2^(a-1) * (5-1)*5^(b-1) = 2^(a+1) * 5^(b-1) = n*2/5
        // phi(n) = phi(2^a*5^0) = 2^(a-1) = n/2
        return (n % 5 == 0) ? n * 2 / 5 : n / 2;
    }

    public static void main(String[] args) {
        System.out.println(solve(1777, 1855, 8));
    }
}
