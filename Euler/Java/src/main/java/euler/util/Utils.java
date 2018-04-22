package euler.util;

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

    public static int exponent(int n, int k) {
        int a = 0;
        while (n % k == 0) {
            a++;
            n /= k;
        }
        return a;
    }

    public static int exponent(long n, long k) {
        int a = 0;
        while (n % k == 0) {
            a++;
            n /= k;
        }
        return a;
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
                if ((b & 1) != 0)
                    r = (r + a) % m;
                a = (a << 1) % m;
                b >>= 1;
            }
        }
        return r % m;
    }

    public static long modpow(long a, long n, long m) {
        long ret = 1;
        while (n > 0) {
            if ((n & 1) == 1)
                ret = modmul(ret, a, m);
            a = modmul(a, a, m);
            n >>= 1;
        }
        return ret;
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
