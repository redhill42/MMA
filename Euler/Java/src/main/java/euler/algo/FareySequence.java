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
     * <p>Given <em>p/q</em> in lowest terms, with <em>p,q ∈ ℕ</em> and <em>q &gt; 0</em>.
     * We want the nearest neighbours of <em>p/q</em>, <em>a/b &lt; p/q &lt; c/d</em>,
     * subject to <em>a,b,c,d ∈ ℕ</em> and <em>c,d &gt; 0</em>.</p>
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
    public Ratio next(Ratio r) {
        long p = r.numer(), q = r.denom();
        long c, d;
        d = q - modinv(p, q);
        d += (n - d) / q * q;
        c = (d * p + 1) / q;
        return new Ratio(c, d);
    }

    /**
     * Get the previous neighbour of p/q in the Farey sequence.
     */
    public Ratio previous(Ratio r) {
        long p = r.numer(), q = r.denom();
        long a, b;
        b = modinv(p, q);
        b += (n - b) / q * q;
        a = (b * p - 1) / q;
        return new Ratio(a, b);
    }

    /**
     * Iterate Farey sequence by given a pair of consecutive terms.
     */
    public Iterable<Ratio> sequence(Ratio a, Ratio b) {
        return new Iterable<Ratio>() {
            @Override
            public Iterator<Ratio> iterator() {
                return new Sequence(a.numer(), a.denom(), b.numer(), b.denom(), n);
            }
        };
    }

    /**
     * Iterate all elements in Farey sequence in ascending order.
     */
    public Iterable<Ratio> ascending() {
        return new Iterable<Ratio>() {
            @Override
            public Iterator<Ratio> iterator() {
                return new Sequence(0, 1, 1, n, n) {
                    @Override
                    public boolean hasNext() {
                        return a <= b;
                    }
                };
            }
        };
    }

    /**
     * Iterate all elements in Farey sequence in descending order.
     */
    public Iterable<Ratio> descending() {
        return new Iterable<Ratio>() {
            @Override
            public Iterator<Ratio> iterator() {
                return new Sequence(1, 1, n - 1, n, n) {
                    @Override
                    public boolean hasNext() {
                        return a >= 0;
                    }
                };
            }
        };
    }

    /**
     * Iterate Farey sequence in ascending order by given the start term.
     */
    public Iterable<Ratio> ascending(Ratio begin) {
        return sequence(begin, next(begin));
    }

    /**
     * Iterate Farey sequence in ascending order by given the start term
     * and ending term.
     */
    public Iterable<Ratio> ascending(Ratio begin, Ratio end) {
        return new Iterable<Ratio>() {
            @Override
            public Iterator<Ratio> iterator() {
                final Ratio next = next(begin);
                final long x = end.numer(), y = end.denom();
                return new Sequence(begin.numer(), begin.denom(),
                                    next.numer(), next.denom(),
                                    n) {
                    @Override
                    public boolean hasNext() {
                        return a * y < b * x;
                    }
                };
            }
        };
    }

    /**
     * Iterate Farey sequence in descending order by given the start term.
     */
    public Iterable<Ratio> descending(Ratio begin) {
        return sequence(begin, previous(begin));
    }

    /**
     * Iterate Farey sequence in descending order by given the start term
     * and ending term.
     */
    public Iterable<Ratio> descending(Ratio begin, Ratio end) {
        return new Iterable<Ratio>() {
            @Override
            public Iterator<Ratio> iterator() {
                final Ratio prev = previous(begin);
                final long x = end.numer(), y = end.denom();
                return new Sequence(begin.numer(), begin.denom(),
                                    prev.numer(), prev.denom(),
                                    n) {
                    @Override
                    public boolean hasNext() {
                        return a * y > b * x;
                    }
                };
            }
        };
    }

    private static class Sequence implements Iterator<Ratio> {
        final long n;
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
        public Ratio next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Ratio r = new Ratio(a, b);

            long k = (n + b) / d;
            long c1 = k * c - a;
            long d1 = k * d - b;

            a = c;
            b = d;
            c = c1;
            d = d1;

            return r;
        }
    }
}
