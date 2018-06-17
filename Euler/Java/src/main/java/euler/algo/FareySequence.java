package euler.algo;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static euler.algo.Library.modinv;

/**
 * The Farey sequence <em>F<sub>n</sub></em> for any positive interger
 * <em>n</em> is the set of irreducible rational numbers <em>a/b</em> with
 * <em>0 ≤ a ≤ b ≤ n</em> and <em>(a,b) = 1</em> arranged in increasing order.
 */
public class FareySequence {
    // the order of Farey sequence.
    private final long n;

    public FareySequence(long n) {
        this.n = n;
    }

    /**
     * <p>Given <em>p/q</em> in lowest terms, with <em>p,q ∈ ℕ</em> and <em>q > 0</em>.
     * We want the nearest neighbours of <em>p/q</em>, <em>a/b < p/q < c/d</em>,
     * subject to <em>a,b,c,d ∈ ℕ</em> and <em>c,d > 0</em>.</p>
     *
     * <p>Consider <em>c/d</em>. It is the element after <em>p/q</em> in the Farey
     * Sequence <em>F<sub>n</sub></em>, which means that <em>cq - dp = 1</em>.
     * In particular, <em>dp + 1 = 0 (mod q)</em>, i.e., <em>d = -1/p (mod q)</em>.
     * So let <em>r = 1/p (mod q)</em>.</p>
     *
     * <p>Now make <em>d</em> as big as possible, subject to (i) <em>d = -r (mod q)</em>
     * and (ii) <em>d ≤ n</em>. Then just put <em>c = (dp + 1)/q</em>.</p>
     *
     * <p>Similarly, to calculate <em>a/b</em>, make <em>b</em> as big as possible
     * subject to (i) <em>b = +r (mod q)</em> and (ii) <em>b ≤ n</em>. Then put
     * <em>a = (bp - 1)/q</em>.</p>
     */
    public Pair next(long p, long q) {
        long c, d;
        d = q - modinv(p, q);
        d += (n - d) / q * q;
        c = (d * p + 1) / q;
        return new Pair(c, d);
    }

    /**
     * Get the next neighbour in the Farey sequence.
     */
    public Pair next(Pair p) {
        return next(p.x, p.y);
    }

    /**
     * Get the previous neighbour of p/q in the Farey sequence.
     */
    public Pair previous(long p, long q) {
        long a, b;
        b = modinv(p, q);
        b += (n - b) / q * q;
        a = (b * p - 1) / q;
        return new Pair(a, b);
    }

    /**
     * Get the previous neighbour in the Farey sequence.
     */
    public Pair previous(Pair p) {
        return previous(p.x, p.y);
    }

    /**
     * Iterate Farey sequence by given consecutive terms of <em>a/b</em>
     * and <em>c/d</em>.
     */
    public Iterable<Pair> sequence(long a, long b, long c, long d) {
        return new Iterable<Pair>() {
            @Override
            public Iterator<Pair> iterator() {
                return new Sequence(a, b, c, d, n);
            }
        };
    }

    /**
     * Iterate Farey sequence by given a pair of consecutive terms.
     */
    public Iterable<Pair> sequence(Pair a, Pair b) {
        return sequence(a.x, a.y, b.x, b.y);
    }

    /**
     * Iterate all elements in Farey sequence in ascending order.
     */
    public Iterable<Pair> ascending() {
        return new Iterable<Pair>() {
            @Override
            public Iterator<Pair> iterator() {
                return new Ascending(n);
            }
        };
    }

    /**
     * Iterate all elements in Farey sequence in descending order.
     */
    public Iterable<Pair> descending() {
        return new Iterable<Pair>() {
            @Override
            public Iterator<Pair> iterator() {
                return new Descending(n);
            }
        };
    }

    /**
     * Iterate Farey sequence in ascending order by given the start term
     * of <em>a/b</em>.
     */
    public Iterable<Pair> ascending(long a, long b) {
        return ascending(new Pair(a, b));
    }

    /**
     * Iterate Farey sequence in ascending order by given the start term
     * in a pair.
     */
    public Iterable<Pair> ascending(Pair begin) {
        return sequence(begin, next(begin));
    }

    /**
     * Iterate Farey sequence in descending order by given the start term
     * of <em>a/b</em>.
     */
    public Iterable<Pair> descending(long a, long b) {
        return descending(new Pair(a, b));
    }

    /**
     * Iterate Farey sequence in descending order by given the start term
     * in a pair.
     */
    public Iterable<Pair> descending(Pair begin) {
        return sequence(begin, previous(begin));
    }

    private static class Sequence implements Iterator<Pair> {
        final long n;
        final Pair pair = new Pair();

        long a, b, c, d;

        Sequence(long a, long b, long c, long d, long n) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
            this.n = n;
        }

        @Override
        public boolean hasNext() {
            return true;
        }

        @Override
        public Pair next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            pair.x = a;
            pair.y = b;

            long k = (n + b) / d;
            long c1 = k * c - a;
            long d1 = k * d - b;

            a = c;
            b = d;
            c = c1;
            d = d1;

            return pair;
        }
    }

    private static class Ascending extends Sequence {
        Ascending(long n) {
            super(0, 1, 1, n, n);
        }

        @Override
        public boolean hasNext() {
            return a <= b;
        }
    }

    private static class Descending extends Sequence {
        Descending(long n) {
            super(1, 1, n - 1, n, n);
        }

        @Override
        public boolean hasNext() {
            return a >= 0;
        }
    }
}
