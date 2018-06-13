package euler.algo;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import static euler.algo.Library.gcd;
import static euler.algo.Library.isqrt;
import static java.lang.Long.min;

@SuppressWarnings("unused")
public final class Pythagorean {
    private Pythagorean() {}

    // Enumerate pythagorean triplet with limitted legs
    public static <V> V withLegs(long limit, V z, BiFunction<V, Triple, V> fn) {
        long max_m = isqrt(2 * limit + 1);
        for (long m = 1; m <= max_m; m += 2) {
            long max_n = min(m - 1, limit / m);
            for (long n = 1; n <= max_n; n += 2) {
                if (gcd(m, n) == 1) {
                    long a = m * n;
                    long b = (m * m - n * n) / 2;
                    long c = (m * m + n * n) / 2;
                    z = fn.apply(z, new Triple(a, b, c));
                }
            }
        }
        return z;
    }

    // Enumerate pythagorean triplet with limitted hypotenuse
    public static <V> V withHypotenuse(long limit, V z, BiFunction<V, Triple, V> fn) {
        long max_m = isqrt(2 * limit - 1);
        for (long m = 1; m <= max_m; m += 2) {
            long max_n = min(m - 1, isqrt(2 * limit - m * m));
            for (long n = 1; n <= max_n; n += 2) {
                if (gcd(m, n) == 1) {
                    long a = m * n;
                    long b = (m * m - n * n) / 2;
                    long c = (m * m + n * n) / 2;
                    z = fn.apply(z, new Triple(a, b, c));
                }
            }
        }
        return z;
    }

    // Enumerate pythagorean triplet with limitted perimeter
    public static <V> V withPerimeter(long limit, V z, BiFunction<V, Triple, V> fn) {
        long max_m = (isqrt(4 * limit + 1) - 1) / 2;
        for (long m = 1; m <= max_m; m += 2) {
            long max_n = min(m - 1, limit / m - m);
            for (long n = 1; n <= max_n; n += 2) {
                if (gcd(m, n) == 1) {
                    long a = m * n;
                    long b = (m * m - n * n) / 2;
                    long c = (m * m + n * n) / 2;
                    z = fn.apply(z, new Triple(a, b, c));
                }
            }
        }
        return z;
    }

    private static Triple f1(long a, long b, long c) {
        return new Triple(2 * c + b - 2 * a,
                           2 * c + 2 * b - a,
                           3 * c + 2 * b - 2 * a);
    }

    private static Triple f2(long a, long b, long c) {
        return new Triple(2 * c + b + 2 * a,
                           2 * c + 2 * b + a,
                           3 * c + 2 * b + 2 * a);
    }

    private static Triple f3(long a, long b, long c) {
        return new Triple(2 * c - 2 * b + a,
                           2 * c - b + 2 * a,
                           3 * c - 2 * b + 2 * a);
    }

    /**
     * Solve the Diophantine equation with the form: x^2+y^2=z^2+k.
     * Given the starting triple as the base solutions.
     */
    public static long solve(long[][] start,
                             Predicate<Triple> pred,
                             Predicate<Triple> cosumer,
                             Comparator<Triple> order)
    {
        Queue<Triple> q = (order != null) ? new PriorityQueue<>(order): new ArrayDeque<>();
        for (long[] t : start) {
            q.offer(new Triple(t[0], t[1], t[2]));
        }

        long count = 0;
        while (!q.isEmpty()) {
            Triple t = q.poll();
            if (pred != null && !pred.test(t))
                continue;
            count++;
            if (cosumer != null && !cosumer.test(t))
                break;

            long a = t.a, b = t.b, c = t.c;
            if (a != b)
                q.offer(f1(a, b, c));
            q.offer(f2(a, b, c));
            q.offer(f3(a, b, c));
        }
        return count;
    }

    /**
     * Convenient method to enumerate triplet with limitted perimeter.
     */
    public static long[][] enumerate(long[][] start, int limit) {
        List<Triple> solutions = new ArrayList<>();
        solve(start, t -> t.perimeter() <= limit, solutions::add, Comparator.naturalOrder());

        long[][] result = new long[solutions.size()][3];
        for (int i = 0; i < result.length; i++) {
            Triple t = solutions.get(i);
            result[i][0] = t.a;
            result[i][1] = t.b;
            result[i][2] = t.c;
        }
        return result;
    }

    /**
     * Convenient method to count triplet with limitted perimeter.
     */
    public static long count(long[][] start, int limit) {
        return solve(start, t -> t.perimeter() <= limit, null, null);
    }
}
