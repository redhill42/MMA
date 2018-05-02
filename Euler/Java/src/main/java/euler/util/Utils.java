package euler.util;

import java.math.BigInteger;

@SuppressWarnings("unused")
public final class Utils {
    private Utils() {}

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

    public static int exgcd(int a, int b, int[] p) {
        if (a < 0) a = -a;
        if (b < 0) b = -b;

        int s0 = 1, s1 = 0;
        int t0 = 0, t1 = 1;
        int q;

        while (true) {
            q = a / b;
            a %= b;
            s0 -= q * s1;
            t0 -= q * t1;
            if (a == 0) {
                p[0] = s1;
                p[1] = t1;
                return b;
            }

            q = b / a;
            b %= a;
            s1 -= q * s0;
            t1 -= q * t0;
            if (b == 0) {
                p[0] = s0;
                p[1] = t0;
                return a;
            }
        }
    }

    public static long exgcd(long a, long b, long[] p) {
        if (a < 0) a = -a;
        if (b < 0) b = -b;

        long s0 = 1, s1 = 0;
        long t0 = 0, t1 = 1;
        long q;

        while (true) {
            q = a / b;
            a %= b;
            s0 -= q * s1;
            t0 -= q * t1;
            if (a == 0) {
                p[0] = s1;
                p[1] = t1;
                return b;
            }

            q = b / a;
            b %= a;
            s1 -= q * s0;
            t1 -= q * t0;
            if (b == 0) {
                p[0] = s0;
                p[1] = t0;
                return a;
            }
        }
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

    public static int pow(int x, int n) {
        if (n == 0)
            return 1;
        if (n == 1)
            return x;

        int y = 1;
        while (n != 0) {
            if (n % 2 == 1)
                y *= x;
            n >>= 1;
            x *= x;
        }
        return y;
    }

    public static long pow(long x, int n) {
        if (n == 0)
            return 1;
        if (n == 1)
            return x;

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

    public static boolean isPrime(int n) {
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

        for (int f = 5, w = isqrt(n); f <= w; f += 6)
            if (n % f == 0 || n % (f + 2) == 0)
                return false;
        return true;
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

    private static double log2(double x) {
        return Math.log(x) / Math.log(2);
    }

    private static final int[] totientSumCache = {
        0, 1, 2, 4, 6, 10, 12, 18, 22, 28, 32
    };

    public static long totientSum(int n) {
        if (n < 0)
            return 0;
        if (n < totientSumCache.length)
            return totientSumCache[n];

        int L = (int)(Math.pow(n / log2(log2(n)), 2./3));
        long[] sieve = new long[L + 1];
        long[] bigV  = new long[n / L + 1];

        for (int i = 0; i < sieve.length; i++) {
            sieve[i] = i;
        }

        for (int p = 2; p <= L; p++) {
            if (p == sieve[p])
                for (int k = p; k <= L; k += p)
                    sieve[k] -= sieve[k] / p;
            sieve[p] += sieve[p - 1];
        }

        for (int x = n / L; x >= 1; x--) {
            int k = n / x, klimit = isqrt(k);
            long res = (long)k * (k + 1) / 2;

            for (int g = 2; g <= klimit; g++) {
                if (k / g <= L) {
                    res -= sieve[k / g];
                } else {
                    res -= bigV[x * g];
                }
            }

            for (int z = 1; z <= klimit; z++) {
                if (z != k / z)
                    res -= (k / z - k / (z + 1)) * sieve[z];
            }

            bigV[x] = res;
        }

        return bigV[1];
    }

    public static long totientSum(long n, long m) {
        if (n < 0)
            return 0;
        if (n < totientSumCache.length)
            return totientSumCache[(int)n] % m;

        int L = (int)(Math.pow(n / log2(log2(n)), 2./3));
        long[] sieve = new long[L + 1];
        long[] bigV  = new long[(int)(n / L + 1)];

        for (int i = 0; i < sieve.length; i++) {
            sieve[i] = i;
        }

        for (int p = 2; p <= L; p++) {
            if (p == sieve[p])
                for (int k = p; k <= L; k += p)
                    sieve[k] -= sieve[k] / p;
            sieve[p] = (sieve[p] + sieve[p - 1]) % m;
        }

        for (int x = (int)(n / L); x >= 1; x--) {
            long k = n / x;
            int  klimit = (int)isqrt(k);
            long res;

            if ((k & 1) == 0) {
                res = modmul(k + 1, k / 2, m);
            } else {
                res = modmul(k, (k + 1) / 2, m);
            }

            for (int g = 2; g <= klimit; g++) {
                if (k / g <= L) {
                    res -= sieve[(int)(k / g)];
                } else {
                    res -= bigV[x * g];
                }
            }

            for (int z = 1; z <= klimit; z++) {
                if (z != k / z) {
                    res -= (k / z - k / (z + 1)) * sieve[z] % m;
                }
            }

            res %= m;
            if (res < 0)
                res += m;
            bigV[x] = res;
        }

        return bigV[1];
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

    public static int reverse(int n) {
        return reverse(n, 10);
    }

    public static int reverse(int n, int b) {
        int r = 0;
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
