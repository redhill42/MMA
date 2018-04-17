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

        long r = isqrt(n), f = 5;
        while (f <= r) {
            if (n % f == 0)
                return false;
            if (n % (f + 2) == 0)
                return false;
            f += 6;
        }
        return true;
    }

    public static int reverse(int n) {
        int r = 0;
        while (n > 0) {
            r = 10 * r + n % 10;
            n /= 10;
        }
        return r;
    }

    public static long reverse(long n) {
        long r = 0;
        while (n > 0) {
            r = 10 * r + n % 10;
            n /= 10;
        }
        return r;
    }

    public static boolean isPalindrome(long n) {
        return n == reverse(n);
    }
}
