package euler;

import static euler.util.Utils.modpow;
import static euler.util.Utils.pow;

public final class Problem282 {
    private Problem282() {}

    public static long solve(int max, long m) {
        long sum = 0;
        for (int n = 0; n <= max; n++)
            sum += ack(n, m);
        return sum % m;
    }

    private static long ack(int n, long m) {
        switch (n) {
        case 0:
            return 1;
        case 1:
            return 3;
        case 2:
            return 7;
        case 3:
            return 61;
        case 4:
            return tetration(n + 3, m) - 3;

        default:
            int cycle = 0;
            long t = m;
            while (t != 1) {
                cycle++;
                while (t % 2 == 0)
                    t /= 2;
                t = totient(t);
            }
            return tetration(cycle, m) - 3;
        }
    }

    private static long tetration(int n, long m) {
        switch (n) {
        case 0: return 1 % m;
        case 1: return 2 % m;
        case 2: return 4 % m;
        case 3: return 16 % m;
        case 4: return 65536 % m;
        }

        long k = 1;
        while ((m & 1) == 0) {
            k <<= 1;
            m >>= 1;
        }

        if (m == 1)
            return 0;

        long y = modpow(2, tetration(n - 1, totient(m)), m);
        while (y % k != 0)
            y += m;
        return y;
    }

    private static long totient(long n) {
        long phi = n;
        for (long p = 2; p * p <= n; p++) {
            if (n % p == 0) {
                phi -= phi / p;
                while (n % p == 0)
                    n /= p;
            }
        }
        if (n != 1) {
            phi -= phi / n;
        }
        return phi;
    }

    public static void main(String[] args) {
        System.out.println(solve(6, pow(14, 8)));
    }
}
