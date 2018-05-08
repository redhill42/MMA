package euler.util;

import java.math.BigInteger;

@SuppressWarnings("unused")
public final class Utils {
    private Utils() {}

    public static boolean even(int n) {
        return (n & 1) == 0;
    }

    public static boolean odd(int n) {
        return (n & 1) == 1;
    }

    public static int gcd(int a, int b) {
        if (a < 0) a = -a;
        if (b < 0) b = -b;
        while (b > 0) {
            int m = a % b;
            a = b;
            b = m;
        }
        return a;
    }

    public static long gcd(long a, long b) {
        if (a < 0) a = -a;
        if (b < 0) b = -b;
        while (b > 0) {
            long m = a % b;
            a = b;
            b = m;
        }
        return a;
    }

    public static int lcm(int a, int b) {
        return a * b / gcd(a, b);
    }

    public static long lcm(long a, long b) {
        return a * b / gcd(a, b);
    }

    public static int exgcd(int a, int b, int[] r) {
        if (a < 0) a = -a;
        if (b < 0) b = -b;

        int x0 = 1, x1 = 0;
        int y0 = 0, y1 = 1;
        int x2, y2, q, t;

        while (b > 0) {
            x2 = x1; y2 = y1; t = b;
            q  = a / b;
            b  = a % b;
            x1 = x0 - q * x1;
            y1 = y0 - q * y1;
            x0 = x2; y0 = y2; a = t;
        }
        r[0] = x0;
        r[1] = y0;
        return a;
    }

    public static long exgcd(long a, long b, long[] r) {
        if (a < 0) a = -a;
        if (b < 0) b = -b;

        long x0 = 1, x1 = 0;
        long y0 = 0, y1 = 1;
        long x2, y2, q, t;

        while (b > 0) {
            x2 = x1; y2 = y1; t = b;
            q  = a / b;
            b  = a % b;
            x1 = x0 - q * x1;
            y1 = y0 - q * y1;
            x0 = x2; y0 = y2; a = t;
        }
        r[0] = x0;
        r[1] = y0;
        return a;
    }

    private static boolean merge(long a, long b, long m, long[] p) {
        if (m < 0)
            m = -m;
        if ((a %= m) < 0)
            a += m;
        if ((b %= m)  < 0)
            b += m;

        long d = exgcd(a, m, p);
        if (b % d != 0)
            return false;

        long x = (p[0] % m + m) % m;
        x = x * (b / d) % m;

        p[0] = m / d;
        p[1] = x % p[0];
        return true;
    }

    public static long chineseRemainder(long a, long n, long b, long m) {
        if (n < 0)
            n = -n;
        if (m < 0)
            m = -m;
        if ((a %= n) < 0)
            a = -a;
        if ((b %= m) < 0)
            b = -b;

        long[] p = new long[2];
        if (!merge(n, b - a, m, p))
            return 0;

        long t = p[0] * n;
        return ((a + p[1] * n) % t + t) % t;
    }

    public static int exponent(long n, long k) {
        int a = 0;
        while (n % k == 0) {
            a++;
            n /= k;
        }
        return a;
    }

    public static long factorialExponent(long n, long p) {
        long s = 0;
        for (n /= p; n != 0; n /= p)
            s += n;
        return s;
    }

    public static long pow(long x, int n) {
        if (n == 0)
            return 1;
        if (n == 1)
            return x;
        if (x == 2)
            return 1L << n;

        long y = 1;
        while (n != 0) {
            if (n % 2 == 1)
                y *= x;
            n >>= 1;
            x *= x;
        }
        return y;
    }

    public static long modmul(long a, long b, long m) {
        if (a == 0 || b == 0) {
            return 0;
        }

        long r = (a %= m) * (b %= m);
        if ((a | b) >>> 31 != 0 && r / b != a) {
            r = 0;
            while (b > 0) {
                if ((b & 1) != 0 && (r += a) >= m)
                    r -= m;
                if ((a <<= 1) >= m)
                    a -= m;
                b >>= 1;
            }
        }
        return r % m;
    }

    public static int modmul(int a, int b, int m) {
        return (int)((long)a * b % m);
    }

    public static long modpow(long a, long n, long m) {
        long x = a, y = 1;
        while (n > 0) {
            if ((n & 1) == 1)
                y = modmul(y, x, m);
            x = modmul(x, x, m);
            n >>= 1;
        }
        return y;
    }

    public static int modpow(int a, int n, int m) {
        long x = a, y = 1;
        while (n > 0) {
            if ((n & 1) == 1)
                y = y * x % m;
            x = x * x % m;
            n >>= 1;
        }
        return (int)y;
    }

    public static long modinv(long x, long m) {
        long y = x, a = 0, b = 1;
        x = m;
        while (y != 0) {
            long z = x % y;
            long c = a - x / y * b;
            x = y; y = z; a = b; b = c;
        }
        if (x == 1) {
            return a >= 0 ? a : a + m;
        } else {
            throw new IllegalArgumentException("Inverse modulo does not exist");
        }
    }

    public static int isqrt(int n) {
        return (int)Math.sqrt(n);
    }

    public static long isqrt(long n) {
        return (long)Math.sqrt(n);
    }

    public static boolean isSquare(long n) {
        if (n < 0) return false;
        long r = isqrt(n);
        return r * r == n;
    }

    public static boolean isPrime(long n) {
        if (n <= 1)
            return false;
        if (n < 4)  // 2 and 3 are prime
            return true;
        if (n % 2 == 0)
            return false;
        if (n < 9)  // we have already excluded 4, 6, and 8
            return true;
        if (n % 3 == 0)
            return false;

        for (long f = 5, w = isqrt(n); f <= w; f += 6)
            if (n % f == 0 || n % (f + 2) == 0)
                return false;
        return true;
    }

    private static final long[][] binomial = new long[64][];
    static {
        for (int i = 0; i < binomial.length; i++) {
            binomial[i] = new long[i + 1];
            binomial[i][0] = binomial[i][i] = 1;
            for (int j = 1; j < i; j++)
                binomial[i][j] = binomial[i-1][j] + binomial[i-1][j-1];
        }
    }

    public static BigInteger choose(int n, int k) {
        if (n < 0 || k < 0 || k > n)
            return BigInteger.ZERO;
        if (n < binomial.length)
            return BigInteger.valueOf(binomial[n][k]);

        BigInteger r = BigInteger.ONE;
        if (k > n / 2)
            k = n - k;
        for (int i = 1; i <= k; i++)
            r = r.multiply(BigInteger.valueOf(n - i + 1))
                 .divide(BigInteger.valueOf(i));
        return r;
    }

    public static long reverse(long n) {
        return reverse(n, 10);
    }

    public static long reverse(long n, int b) {
        long r = 0;
        if (b == 2) {
            while (n != 0) {
                r = (r << 1) | (n & 1);
                n >>= 1;
            }
        } else {
            while (n != 0) {
                r = b * r + n % b;
                n /= b;
            }
        }
        return r;
    }

    public static boolean isPalindrome(long n) {
        return n == reverse(n);
    }
}
