package euler;

import euler.util.TotientSieve;

public final class Problem531 {
    private Problem531() {}

    static class Pair {
        long x, y;
    }

    static long exgcd(long a, long b, Pair p) {
        if (b == 0) {
            p.x = 1;
            p.y = 0;
            return a;
        } else {
            long r = exgcd(b, a % b, p);
            long t = p.x;
            p.x = p.y;
            p.y = t - a / b *p.y;
            return r;
        }
    }

    static boolean merge(long a, long b, long m, Pair p) {
        if (m < 0)
            m = -m;
        if ((a %= m) < 0)
            a += m;
        if ((b %= m)  < 0)
            b += m;

        long d = exgcd(a, m, p);
        if (b % d != 0)
            return false;

        long x = (p.x % m + m) % m;
        x = x * (b / d) % m;

        p.x = m / d;
        p.y = x % p.x;
        return true;
    }

    static long chineseRemainder(long a, long n, long b, long m) {
        if (n < 0)
            n = -n;
        if (m < 0)
            m = -m;
        if ((a %= n) < 0)
            a = -a;
        if ((b %= m) < 0)
            b = -b;

        Pair p = new Pair();
        if (!merge(n, b - a, m, p))
            return 0;

        long t = p.x * n;
        return ((a + p.y * n) % t + t) % t;
    }

    public static long solve(int start, int end) {
        TotientSieve sieve = new TotientSieve(end);
        long sum = 0;
        for (int n = start; n < end; n++) {
            for (int m = n + 1; m < end; m++) {
                sum += chineseRemainder(sieve.phi(n), n, sieve.phi(m), m);
            }
        }
        return sum;
    }

    public static void main(String[] args) {
        System.out.println(solve(1_000_000, 1_005_000));
    }
}
