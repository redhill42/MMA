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
    public static <V> V withHypotenuse(long limit, V z, TripletFunction<V> fn) {
        long max_m = isqrt(limit);
        for (long m = 1; m <= max_m; m++) {
            long max_n = min(m - 1, isqrt(limit - m * m));
            for (long n = (m & 1) + 1; n <= max_n; n += 2) {
                if (gcd(m, n) == 1) {
                    long a = m * m - n * n;
                    long b = 2 * m * n;
                    long c = m * m + n * n;
                    z = fn.compute(z, a, b, c);
                }
            }
        }
        return z;
    }

    // Enumerate pythagorean triplet with limitted perimeter
    public static <V> V withPerimeter(long limit, V z, TripletFunction<V> fn) {
        long max_m = isqrt(limit / 2);
        for (long m = 1; m <= max_m; m++) {
            long max_n = min(m - 1, limit / m / 2 - m);
            for (long n = (m & 1) + 1; n <= max_n; n += 2) {
                if (gcd(m, n) == 1) {
                    long a = m * m - n * n;
                    long b = 2 * m * n;
                    long c = m * m + n * n;
                    z = fn.compute(z, a, b, c);
                }
            }
        }
        return z;
    }
}
