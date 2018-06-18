package euler.algo;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

import static euler.algo.Library.factorize;
import static euler.algo.Library.gcd;
import static euler.algo.Library.modpow;
import static euler.algo.Library.pow;

/**
 * The Factorization contains a list of prime factors of a given integer.
 * Many number theory algorithms are based on integer factorization.
 */
@SuppressWarnings("unused")
public class Factorization implements Iterable<PrimeFactor> {
    private final long n;
    private final List<PrimeFactor> factors;

    /**
     * Construct a new number factorization.
     *
     * @param n the number to be factorized
     * @param factors the factors of the number
     */
    public Factorization(long n, List<PrimeFactor> factors) {
        this.n = n;
        this.factors = factors;
    }

    /**
     * The totient function <em>ϕ(n)</em>, also called Euler's totient function,
     * is defined as the number of positive integers <em>≤ n</em> that are relatively
     * coprime to (i.e., do not contain any factor in common with) <em>n</em>, where
     * 1 is counted as being relatively prime to all numbers.
     *
     * @return the totient function phi(n)
     */
    public long phi() {
        long r = this.n;
        for (PrimeFactor f : factors) {
            long p = f.prime();
            r = r / p * (p - 1);
        }
        return r;
    }

    /**
     * The number of divisors of <em>n</em>. The notation <em>d(n)</em>, and
     * <em>σ<sub>0</sub>(n)</em> are sometimes used for <em>τ(n)</em>.
     *
     * @return the number of divisors of n
     */
    public int tau() {
        int r = 1;
        for (PrimeFactor f : factors)
            r *= f.power() + 1;
        return r;
    }

    /**
     * The divisor function <em>σ<sub>k</sub>(n)</em> for <em>n</em> an integer
     * is defined as the sum of the kth powers of the (positive integer) divisors
     * of <em>n</em>.
     *
     * @param k the power
     * @return the divisor function
     */
    public long sigma(int k) {
        long r = 1;
        for (PrimeFactor f : factors) {
            long p = f.prime();
            int  a = f.power();
            long s = 1;
            for (int i = 1; i <= a; i++)
                s += pow(p, i * k);
            r *= s;
        }
        return r;
    }

    /**
     * <p>The unitary divisor is a divisor <em>d</em> of <em>n</em> for which
     * <em>GCD(n,n/d)=1</em>, where <em>GCD(m,n)</em> is the greatest common
     * divisor.</p>
     *
     * <p>The unitary divisor function <em>σ<sup>*</sup><sub>k</sub>(n)</em>
     * is the analog of the {@link #sigma(int) divisor function}
     * <em>σ<sub>k</sub>(n)</em> for unitary divisors and denotes the
     * sum of kth powers of the unitary divisor function.</p>
     *
     * @param k the power
     * @return the unitary divisor function
     */
    public long unitarySigma(int k) {
        long r = 1;
        for (PrimeFactor f : factors)
            r *= 1 + pow(f.prime(), k * f.power());
        return r;
    }

    /**
     * <p>The radical of a positive integer <em>n</em> is defined as the product
     * of the distinct prime numbers dividing <em>n</em>. Each prime factor of
     * <em>n</em> occurs exactly once as a factor of this product.</p>
     *
     * <p>The radical plays a central role in the statement of the abc conjecture.</p>
     *
     * @return the radical of <em>n</em>
     */
    public long rad() {
        long r = 1;
        for (PrimeFactor f : factors)
            r *= f.prime();
        return r;
    }

    /**
     * <p>The Möbius function is a number theoretic function defined by:</p>
     * <ul>
     *     <li><em>μ(n)=0</em> if <em>n</em> has one or more repeated prime factors</li>
     *     <li><em>μ(n)=1</em> if <em>n=1</em></li>
     *     <li><em>μ(n)=(-1)<sup>k</sup></em> if <em>n</em> is a product of
     *     <em>k</em> distinct primes</li>
     * </ul>
     *
     * <p>So <em>μ(n)≠0</em> indicates that <em>n</em> is squarefree.</p>
     *
     * @return the Möbius function
     */
    public int moebius() {
        for (PrimeFactor f : factors)
            if (f.power() > 1)
                return 0;
        return factors.size() % 2 == 0 ? 1 : -1;
    }

    /**
     * The number of prime factors (with repetition) of a positive integer
     * <em>n</em>.
     *
     * @return the number of prime factors of <em>n</em>
     */
    public int omega() {
        int r = 0;
        for (PrimeFactor f : factors)
            r += f.power();
        return r;
    }

    /**
     * The number of distinct prime factors of a positive integer <em>n</em>.
     *
     * @return the number of distinct prime factors of <em>n</em>
     */
    public int nu() {
        return factors.size();
    }

    /**
     * A divisor, also called a factor, of a number <em>n</em> is a number
     * <em>d</em> which divides <em>n</em> (written <em>d|n</em>). This method
     * returns a list of (positive) divisors of a given integer <em>n</em>.
     *
     * @return a list of divisors of <em>n</em>
     */
    public long[] divisors() {
        long[] divisors = new long[tau()];
        divisors[0] = 1;

        int i = 1;
        for (PrimeFactor f : factors) {
            int  k = i;
            long p = f.prime();
            int  a = f.power();
            long q = p;

            while (--a >= 0) {
                for (int j = 0; j < i; j++)
                    divisors[k++] = divisors[j] * q;
                q *= p;
            }
            i = k;
        }

        Arrays.sort(divisors);
        return divisors;
    }

    /**
     * <p>The unitary divisor is a divisor <em>d</em> of <em>n</em> for which
     * <em>GCD(n,n/d)=1</em>, where <em>GCD(m,n)</em> is the greatest common
     * divisor.</p>
     *
     * @return a list of unitary divisors of <em>n</em>
     */
    public long[] unitaryDivisors() {
        long[] divisors = new long[1 << nu()];
        divisors[0] = 1;

        int i = 1;
        for (PrimeFactor f : factors) {
            int  k = i;
            long u = f.value();

            for (int j = 0; j < i; j++)
                divisors[k++] = divisors[j] * u;
            i = k;
        }

        Arrays.sort(divisors);
        return divisors;
    }

    /**
     * The smallest exponent <em>k</em> for which <em>a<sup>k</sup>≡1 (mod n)</em>,
     * where <em>a</em> and <em>n</em> are given numbers, is called the multiplicative
     * order.
     *
     * @param a the given number
     * @return the multiplicative order of <em>a</em> modulo <em>n</em>
     */
    public long ord(int a) {
        if (gcd(a, n) != 1)
            throw new IllegalArgumentException(
                String.format("Multiplicative order: %d and %d must coprime", a, n));

        long n1 = phi();
        long t = 1;
        for (PrimeFactor f : factorize(n1)) {
            long p = f.prime();
            long x = modpow(a, n1 / f.value(), n);
            while (x > 1) {
                x = modpow(x, p, n);
                t *= p;
            }
        }
        return t;
    }

    /**
     * A number <em>g</em> is a primitive root modulo <em>n</em> if every number
     * <em>a</em> coprime to <em>n</em> is congruent to a power of <em>g</em>
     * modulo <em>n</em>. That is, for every integer <em>a</em> coprime to
     * <em>n</em>, there is an integer <em>k</em> such that <em>g<sup>k</sup>≡a (mod n)</em>.
     * Such <em>k</em> is called the index or discrete logarithm of <em>a</em>
     * to the base <em>g</em> modulo <em>n</em>.
     *
     * @param a the given number
     * @return true if <em>a</em> is a primitive root modulo <em>n</em>, false otherwise.
     */
    public boolean isPrimitiveRoot(int a) {
        return ord(a) == phi();
    }

    /**
     * Returns an iterator over elements of prime factors of <em>n</em>
     */
    @Override
    public Iterator<PrimeFactor> iterator() {
        return factors.iterator();
    }

    /**
     * Performs the given action for each element of prime factors of <em>n</em>.
     */
    @Override
    public void forEach(Consumer<? super PrimeFactor> action) {
        factors.forEach(action);
    }

    /**
     * Creates a Spliterator over the elementsof prime factors of <em>n</em>.
     */
    @Override
    public Spliterator<PrimeFactor> spliterator() {
        return factors.spliterator();
    }

    /**
     * Returns the String representation of an integer factorization.
     */
    public String toString() {
        return factors.toString();
    }
}
