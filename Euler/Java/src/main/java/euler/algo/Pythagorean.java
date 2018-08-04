package euler.algo;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import static euler.algo.Library.isCoprime;
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
                if (isCoprime(m, n)) {
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
                if (isCoprime(m, n)) {
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
                if (isCoprime(m, n)) {
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
     * Return the solutions as an Iterable.
     */
    public static Iterable<Triple> solve(long[][] start, Predicate<Triple> filter, Comparator<Triple> order) {
        return new Iterable<Triple>() {
            @Override
            public Iterator<Triple> iterator() {
                return new Itr(start, filter, order);
            }
        };
    }

    public static Iterable<Triple> solve(long[][] start, Predicate<Triple> filter) {
        return solve(start, filter, Comparator.naturalOrder());
    }

    private static class Itr implements Iterator<Triple> {
        private final Predicate<Triple> filter;
        private final Queue<Triple> q;
        private Triple current;

        Itr(long[][] start, Predicate<Triple> filter, Comparator<Triple> order) {
            this.filter = filter;

            q = (order != null) ? new PriorityQueue<>(order) : new ArrayDeque<>();
            for (long[] t : start)
                q.offer(new Triple(t[0], t[1], t[2]));
            advance();
        }

        private void advance() {
            current = null;
            while (!q.isEmpty()) {
                Triple t = q.poll();
                if (filter == null || filter.test(t)) {
                    current = t;
                    break;
                }
            }

            if (current != null) {
                long a = current.a, b = current.b, c = current.c;
                if (a != b)
                    q.offer(f1(a, b, c));
                q.offer(f2(a, b, c));
                q.offer(f3(a, b, c));
            }
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Triple next() {
            Triple t = current;
            if (t == null)
                throw new NoSuchElementException();
            advance();
            return t;
        }
    }
}
