package euler.util;

import static euler.util.Utils.gcd;
import static euler.util.Utils.isqrt;
import static java.lang.Long.min;

@SuppressWarnings("unused")
public final class Pythagorean {
    private Pythagorean() {}

    public interface TripletFunction<V> {
        V compute(V z, long a, long b, long c);
    }

    // Enumerate pythagorean triplet with limitted hypotenuse
    public static <V> V withSides(long limit, V z, TripletFunction<V> fn) {
        long max_m = isqrt(2 * limit + 1);
        for (long m = 1; m <= max_m; m += 2) {
            long max_n = min(m - 1, limit / m);
            for (long n = 1; n <= max_n; n += 2) {
                if (gcd(m, n) == 1) {
                    long a = m * n;
                    long b = (m * m - n * n) / 2;
                    long c = (m * m + n * n) / 2;
                    z = fn.compute(z, a, b, c);
                }
            }
        }
        return z;
    }

    // Enumerate pythagorean triplet with limitted hypotenuse
    public static <V> V withHypotenuse(long limit, V z, TripletFunction<V> fn) {
        long max_m = isqrt(2 * limit - 1);
        for (long m = 1; m <= max_m; m += 2) {
            long max_n = min(m - 1, isqrt(2 * limit - m * m));
            for (long n = 1; n <= max_n; n += 2) {
                if (gcd(m, n) == 1) {
                    long a = m * n;
                    long b = (m * m - n * n) / 2;
                    long c = (m * m + n * n) / 2;
                    z = fn.compute(z, a, b, c);
                }
            }
        }
        return z;
    }

    // Enumerate pythagorean triplet with limitted perimeter
    public static <V> V withPerimeter(long limit, V z, TripletFunction<V> fn) {
        long max_m = (isqrt(4 * limit + 1) - 1) / 2;
        for (long m = 1; m <= max_m; m += 2) {
            long max_n = min(m - 1, limit / m - m);
            for (long n = 1; n <= max_n; n += 2) {
                if (gcd(m, n) == 1) {
                    long a = m * n;
                    long b = (m * m - n * n) / 2;
                    long c = (m * m + n * n) / 2;
                    z = fn.compute(z, a, b, c);
                }
            }
        }
        return z;
    }
}
