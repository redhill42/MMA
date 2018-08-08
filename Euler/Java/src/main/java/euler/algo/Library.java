package euler.algo;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SuppressWarnings("unused")
public final class Library {
    private Library() {}

    public static boolean even(int n) {
        return (n & 1) == 0;
    }

    public static boolean even(long n) {
        return (n & 1) == 0;
    }

    public static boolean odd(int n) {
        return (n & 1) == 1;
    }

    public static boolean odd(long n) {
        return (n & 1) == 1;
    }

    public static int gcd(int a, int b) {
        if (a < 0) a = -a;
        if (b < 0) b = -b;
        if (a == 0) return b;
        if (b == 0) return a;

        int shift = Integer.numberOfTrailingZeros(a | b);
        a >>= Integer.numberOfTrailingZeros(a);

        do {
            b >>= Integer.numberOfTrailingZeros(b);
            if (a > b) {
                int t = b;
                b = a - b;
                a = t;
            } else {
                b -= a;
            }
        } while (b != 0);
        return a << shift;
    }

    public static long gcd(long a, long b) {
        if (a < 0) a = -a;
        if (b < 0) b = -b;
        if (a == 0) return b;
        if (b == 0) return a;

        int shift = Long.numberOfTrailingZeros(a | b);
        a >>= Long.numberOfTrailingZeros(a);

        do {
            b >>= Long.numberOfTrailingZeros(b);
            if (a > b) {
                long t = b;
                b = a - b;
                a = t;
            } else {
                b -= a;
            }
        } while (b != 0);
        return a << shift;
    }

    public static boolean isCoprime(int a, int b) {
        if (((a|b) & 1) == 0)
            return false;
        return gcd(a, b) == 1;
    }

    public static boolean isCoprime(long a, long b) {
        if (((a|b) & 1) == 0)
            return false;
        return gcd(a, b) == 1;
    }

    public static int gcd(int x, int... xs) {
        int res = x;
        for (int a : xs)
            res = gcd(res, a);
        return res;
    }

    public static long gcd(long x, long... xs) {
        long res = x;
        for (long a : xs)
            res = gcd(res, a);
        return res;
    }

    public static int lcm(int a, int b) {
        return a * b / gcd(a, b);
    }

    public static long lcm(long a, long b) {
        return a * b / gcd(a, b);
    }

    public static long lcm(long x, long... xs) {
        long res = x;
        for (long a : xs)
            res = lcm(res, a);
        return res;
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
        if (a < 0 || b < 0) {
            long g = exgcd(Math.abs(a), Math.abs(b), r);
            if (a < 0)
                r[0] = -r[0];
            if (b < 0)
                r[1] = -r[1];
            return g;
        }

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

    public static long chineseRemainder(long a, long n, long b, long m) {
        if (n < 0)
            n = -n;
        if (m < 0)
            m = -m;

        a = mod(a, n);
        b = mod(b, m);

        long[] p = new long[2];
        long d = exgcd(n, m, p);
        if ((b - a) % d != 0)
            return -1;
        m /= d;

        long x = mod(p[0] * ((b - a) / d), m);
        return (a + x * n) % (m * n);
    }

    public static long chineseRemainder(long[] b, long[] m) {
        if (b.length != m.length)
            throw new IllegalArgumentException(
                "chineseRemainder: Remainders and modulus doesn't have same length");

        if (m.length == 0)
            return -1;

        long a = b[0], n = m[0];
        for (int i = 1; i < m.length; i++) {
            a = chineseRemainder(a, n, b[i], m[i]);
            if (a == -1)
                break;
            n *= m[i];
        }
        return a;
    }

    public static int exponent(long n, long k) {
        if (k == 2)
            return Long.numberOfTrailingZeros(n);

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
            if ((n & 1) == 1)
                y *= x;
            n >>= 1;
            x *= x;
        }
        return y;
    }

    private static final long LO_MASK = 0xFFFFFFFFL;
    private static final int HI_SHIFT = 32;

    public static long mul128(long x, long y) {
        if ((x | y) >>> 31 == 0)
            return x * y;

        long x0 = x & LO_MASK;
        long x1 = x >>> HI_SHIFT;
        long y0 = y & LO_MASK;
        long y1 = y >>> HI_SHIFT;
        long t  = (x1 * y0) + (x0 * y0 >>> HI_SHIFT);
        long w1 = t & LO_MASK;
        long w2 = t >>> HI_SHIFT;

        long u = (x1 * y1 + w2) + ((x0 * y1 + w1) >>> HI_SHIFT);
        long v = x * y;
        return (u == 0 && v >= 0) ? v : Long.MAX_VALUE;
    }

    public static int mod(long n, int m) {
        if ((n < 0 || n >= m) && (n %= m) < 0)
            n += m;
        return (int)n;
    }

    public static long mod(long n, long m) {
        if ((n < 0 || n >= m) && (n %= m) < 0)
            n += m;
        return n;
    }

    public static long modmul(long a, long b, long m) {
        if (a == 0 || b == 0)
            return 0;
        if (((a = mod(a, m)) | (b = mod(b, m))) >>> 31 == 0)
            return mod(a * b, m);

        long r = 0;
        while (b > 0) {
            if ((b & 1) != 0 && (r += a) >= m)
                r -= m;
            if ((a <<= 1) >= m)
                a -= m;
            b >>= 1;
        }
        return r;
    }

    public static int modmul(int a, int b, int m) {
        return mod((long)a * b, m);
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
            return a < 0 ? a + m : a;
        } else {
            throw new IllegalArgumentException(
                String.format("Inverse modulo does not exist: %d^-1 (mod %d)", x, m));
        }
    }

    private static final Random random = new Random();

    /**
     * Find modular square root using Tonelli Shanks algorithm, i.e.,
     * solve the congruence <em>x<sup>2</sup> ≡ n (mod p)</em>.
     *
     * @param n the number has a square root modulo p
     * @param p an odd prime
     * @return R satisfying <em>R<sup>2</sup> ≡ n (mod p)</em>.
     */
    public static int modsqrt(int n, int p) {
        if (n % p == 0)
            return 0;
        if (!hasSquareRoot(n, p))
            return -1; // no solution

        // Compute Q,S from p - 1 = Q2^s with Q odd.
        int s = exponent(p - 1, 2);
        int q = (p - 1) >> s;

        // If p == 3 (mod 4), then no need to use Tonelli-Shanks to find
        // the answer. We can compute the solution more simply as
        // R == n^((p+1)/4) (mod p). Note that p == 3 (mod 4) if and only
        // if S = 1 int the previous step.
        if (s == 1)
            return modpow(n, (p + 1) / 4, p);

        // Find a number z that has no square root modulo p, i.e. is a quadratic
        // nonresidue modulo p. Wikipedia says that there is no deterministic
        // algorithm that runs in polynomial time for finding such a number.
        // Luckily, with the Legendre symbol, we know that half of the number
        // 1, 2, ..., p-1 are quadratic residues and the other half are quadratic
        // nonresidues, so on average we only have to check two random numbers
        // 1 <= z < p to find a valid z.
        int z;
        do {
            z = random.nextInt(p - 1) + 1;
        } while (modpow(z, (p - 1) / 2, p) != p - 1);

        int c = modpow(z, q, p);
        int t = modpow(n, q, p);
        int r = modpow(n, (q + 1) / 2, p);
        int m = s;

        while (t != 1) {
            // Find the lowest 0 < i < M such that t^2^i == 1 (mod p),
            // e.g., via repeated squaring.
            int i = 0, t1 = t;
            while (t1 != 1) {
                t1 = (int)((long)t1 * t1 % p);
                i++;
            }

            int b = modpow(c, 1 << (m - i - 1), p);
            c = (int)((long)b * b % p);
            t = (int)((long)t * c % p);
            r = (int)((long)r * b % p);
            m = i;
        }
        return r;
    }

    private static boolean hasSquareRoot(int n, int p) {
        if (n == (p + 1) / 2)
            return (p & 7) == 1 || (p & 7) == 7;
        return modpow(n, (p - 1) / 2, p) == 1;
    }

    /**
     * Jacobi symbol (and Legendre symbol) computation
     */
    public static int jacobi(long a, long n) {
        if (a == 0)
            return 0;
        if (a == 1)
            return 1;

        int e = exponent(a, 2);
        long a1 = a >> e;

        int s;
        if (even(e)) {
            s = 1;
        } else {
            switch ((int)(n % 8)) {
            case 1: case 7:
                s = 1;
                break;
            case 3: case 5:
                s = -1;
                break;
            default:
                s = 0;
                break;
            }
        }

        if (n % 4 == 3 && a1 % 4 == 3)
            s = -s;
        if (a1 == 1)
            return s;
        if (s == 0)
            return 0;
        return s * jacobi(n % a1, a1);
    }

    public static int isqrt(int n) {
        return (int)Math.sqrt(n);
    }

    public static long isqrt(long n) {
        return (long)Math.sqrt(n);
    }

    public static long isqrtExact(long n) {
        // "bit" starts with the highest power of four <= n
        int  b   = 63 - Long.numberOfLeadingZeros(n);
        long bit = 1L << (b - (b & 1));
        long res = 0;

        while (bit != 0) {
            long t = res + bit;
            res >>= 1;
            if (n >= t) {
                n -= t;
                res += bit;
            }
            bit >>= 2;
        }
        return res;
    }

    public static BigInteger isqrt(BigInteger n) {
        // "bit" starts at the highest power of four <= n
        int b = n.bitLength() - 1;
        BigInteger bit = BigInteger.ONE.shiftLeft(b - (b & 1));
        BigInteger res = BigInteger.ZERO;

        while (bit.signum() != 0) {
            BigInteger t = res.add(bit);
            res = res.shiftRight(1);
            if (n.compareTo(t) >= 0) {
                n = n.subtract(t);
                res = res.add(bit);
            }
            bit = bit.shiftRight(2);
        }
        return res;
    }

    public static int icbrt(long n) {
        return (int)Math.cbrt(n);
    }

    public static int isurd(int x, int n) {
        return (int)Math.pow(x, 1.0 / n);
    }

    public static long isurd(long x, int n) {
        return (long)Math.pow(x, 1.0 / n);
    }

    public static boolean isSquare(long n) {
        if (n < 0) return false;
        long r = isqrt(n);
        return r * r == n;
    }

    public static boolean isPowerOfTwo(long x) {
        return (x & (x - 1)) == 0;
    }

    private static final long smallPrimes =
        (1L <<  2) | (1L <<  3) | (1L <<  5) | (1L <<  7) | (1L << 11) |
        (1L << 13) | (1L << 17) | (1L << 19) | (1L << 23) | (1L << 29) |
        (1L << 31) | (1L << 37) | (1L << 41) | (1L << 43) | (1L << 47) |
        (1L << 53) | (1L << 59) | (1L << 61);

    // Deterministic variants of the Miller-Rabin primality test
    // https://en.wikipedia.org/wiki/Miller–Rabin_primality_test#Deterministic_variants
    private static final int[] TestBase1 = { 377687 };
    private static final int[] TestBase2 = { 31, 73 };
    private static final int[] TestBase3 = { 2, 7, 61 };
    private static final int[] TestBase4 = { 2, 13, 23, 1662803 };
    // at least 2^64, http://miller-rabin.appspot.com
    private static final int[] TestBase7 = { 2, 325, 9375, 28178, 450775, 9780504, 1795265022 };

    public static boolean isPrime(long n) {
        if (n < 0)
            return false;
        if (n < 64)
            return (smallPrimes & (1L << n)) != 0;
        if (even(n))
            return false;

        int[] testBase;
        if (n < 5329)
            testBase = TestBase1;
        else if (n < 9_080_191)
            testBase = TestBase2;
        else if (n < 4_759_123_141L)
            testBase = TestBase3;
        else if (n < 1_122_004_669_633L)
            testBase = TestBase4;
        else
            testBase = TestBase7;

        int  s = exponent(n - 1, 2);
        long r = (n - 1) >> s;

    WitnessLoop:
        for (int a : testBase) {
            long x = modpow(a, r, n);
            if (x == 1 || x == n - 1)
                continue;
            for (int i = 1; i < s; i++) {
                x = modmul(x, x, n);
                if (x == 1)
                    return false;
                if (x == n - 1)
                    continue WitnessLoop;
            }
            return false;
        }
        return true;
    }

    public static long nextPrime(long n) {
        if (n < 2)
            return 2;
        if (even(++n))
            ++n;
        while (n > 0 && !isPrime(n))
            n += 2;
        return n;
    }

    public static long previousPrime(long n) {
        if (n == 3)
            return 2;
        if (n <= 2)
            return -1;
        if (even(--n))
            --n;
        while (!isPrime(n))
            n -= 2;
        return n;
    }

    private static final int[] wheel = {4, 2, 4, 2, 4, 6, 2, 6};

    public static Factorization factorize(long n) {
        return factorize(n, Integer.MAX_VALUE);
    }

    public static Factorization factorize(long n, int limit) {
        if (isPrime(n)) {
            PrimeFactor[] factors = {new PrimeFactor(n, 1)};
            return new Factorization(factors);
        }

        List<PrimeFactor> factors = new ArrayList<>();

        if (n % 2 == 0) {
            int a = exponent(n, 2);
            factors.add(new PrimeFactor(2, a));
            n >>= a;
        }
        if (n % 3 == 0)
            n = addPrimeFactor(n, 3, factors);
        if (n % 5 == 0)
            n = addPrimeFactor(n, 5, factors);

        int maxFactor = (int)Math.min(limit, isqrt(n));
        int p = 7, i = 0;
        while (p <= maxFactor) {
            if (n % p == 0) {
                n = addPrimeFactor(n, p, factors);
                maxFactor = (int)Math.min(limit, isqrt(n));
            }
            p += wheel[i];
            i = (i + 1) & 7;
        }

        if (n != 1 && n < limit) {
            factors.add(new PrimeFactor(n, 1));
        }

        return new Factorization(factors);
    }

    private static long addPrimeFactor(long n, int p, List<PrimeFactor> factors) {
        int a = 0;
        while (n % p == 0) {
            a++;
            n /= p;
        }
        factors.add(new PrimeFactor(p, a));
        return n;
    }

    public static long factorial(int n) {
        long r = 1;
        for (int i = 2; i <= n; i++)
            r *= i;
        return r;
    }

    public static long[] factorials(int n) {
        long[] r = new long[n + 1];
        r[0] = r[1] = 1;
        for (int i = 2; i <= n; i++)
            r[i] = i * r[i - 1];
        return r;
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
            return big(binomial[n][k]);

        BigInteger r = BigInteger.ONE;
        if (k > n / 2)
            k = n - k;
        for (int i = 1; i <= k; i++)
            r = r.multiply(big(n - i + 1)).divide(big(i));
        return r;
    }

    public static long fibonacci(int n) {
        if (n < 0) {
            long f = fibonacci(-n);
            return even(n) ? -f : f;
        }

        long a = 0, b = 1;
        while (n-- > 0) {
            long t = b;
            b = a + b;
            a = t;
        }
        return a;
    }

    public static long fibonacciMod(long n, long m, long[] r) {
        if (n < 0)
            throw new UnsupportedOperationException("not implemented");

        if (n == 0) {
            if (r != null) {
                r[0] = 1; r[1] = 0; r[2] = 0; r[3] = 1; // identity matrix
            }
            return 0;
        }
        n--;

        long a = 1, b = 1, c = 1, d = 0;
        long x = 1, y = 1, z = 1, w = 0;

        for (; n > 0; n >>= 1) {
            if ((n & 1) != 0) {
                long x1 = (modmul(a, x, m) + modmul(c, y, m)) % m;
                long y1 = (modmul(b, x, m) + modmul(d, y, m)) % m;
                long z1 = (modmul(c, w, m) + modmul(a, z, m)) % m;
                long w1 = (modmul(d, w, m) + modmul(b, z, m)) % m;
                x = x1; y = y1; z = z1; w = w1;
            }

            long a1 = (modmul(a, a, m) + modmul(c, b, m)) % m;
            long b1 = (modmul(b, a, m) + modmul(d, b, m)) % m;
            long c1 = (modmul(a, c, m) + modmul(c, d, m)) % m;
            long d1 = (modmul(b, c, m) + modmul(d, d, m)) % m;
            a = a1; b = b1; c = c1; d = d1;
        }

        if (r != null) {
            r[0] = x; r[1] = y; r[2] = z; r[3] = w;
        }
        return y;
    }

    /**
     * Returns the nth triangular number modulo m
     */
    public static long tri(long n, long m) {
        return even(n) ? modmul(n >> 1, n + 1, m)
                       : modmul(n, (n + 1) >> 1, m);
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

    public static long fromDigits(int[] digits) {
        return fromDigits(digits, 10, 0, digits.length);
    }

    public static long fromDigits(int[] digits, int from, int to) {
        return fromDigits(digits, 10, from, to);
    }

    public static long fromDigits(int[] digits, int radix) {
        return fromDigits(digits, radix, 0, digits.length);
    }

    public static long fromDigits(int[] digits, int radix, int from, int to) {
        long n = 0;
        for (int i = from; i < to; i++)
            n = n * radix + digits[i];
        return n;
    }

    public static int digitSum(BigInteger n) {
        String digits = n.toString();
        int s = 0;
        for (int i = digits.length(); --i >= 0; )
            s += digits.charAt(i) - '0';
        return s;
    }

    public static BigInteger big(long n) {
        return BigInteger.valueOf(n);
    }

    private static final String[] roman_rules = {
         "M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"
    };
    private static final int[] roman_values = {
        1000, 900, 500,  400, 100,   90,  50,   40,  10,    9,   5,    4,   1
    };

    public static String toRomanNumeral(int n) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < roman_rules.length; i++) {
            while (n >= roman_values[i]) {
                result.append(roman_rules[i]);
                n -= roman_values[i];
            }
        }
        return result.toString();
    }

    public static int fromRomanNumeral(String roman) {
        int last = 0; // the previous value of Roman letter
        boolean subtract = false; // true if current letter is subtracted
        int res = 0;

        for (int i = roman.length() - 1; i >= 0; i--) {
            int c;
            switch (roman.charAt(i)) {
            case 'M': c = 1000; break;
            case 'D': c =  500; break;
            case 'C': c =  100; break;
            case 'L': c =   50; break;
            case 'X': c =   10; break;
            case 'V': c =    5; break;
            case 'I': c =    1; break;
            default:
                throw new IllegalArgumentException("Invalid Roman numeral: " + roman);
            }

            if (c < last) {
                subtract = true;
                last = c;
            } else if (c > last) {
                subtract = false;
                last = c;
            }
            res = subtract ? res - c : res + c;
        }
        return res;
    }
}
