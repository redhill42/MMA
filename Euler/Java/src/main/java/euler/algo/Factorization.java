package euler.algo;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;

import static euler.algo.Library.factorize;
import static euler.algo.Library.isCoprime;
import static euler.algo.Library.modpow;
import static euler.algo.Library.pow;

/**
 * The Factorization contains a list of prime factors of a given integer.
 * Many number theory algorithms are based on integer factorization.
 */
@SuppressWarnings("unused")
public class Factorization implements Iterable<PrimeFactor> {
    private final PrimeFactor[] factors;

    /**
     * Construct a new factorization with a single prime factor.
     *
     * @param p the prime number
     * @param a the power
     */
    public Factorization(long p, int a) {
        factors = new PrimeFactor[1];
        factors[0] = new PrimeFactor(p, a);
    }

    /**
     * Construct a new number factorization.
     *
     * @param factors the factors of the number
     */
    public Factorization(PrimeFactor[] factors) {
        this.factors = factors;
    }

    /**
     * Construct a new number factorization.
     *
     * @param factors the factors of the number
     */
    public Factorization(Collection<PrimeFactor> factors) {
        PrimeFactor[] a = new PrimeFactor[factors.size()];
        factors.toArray(a);
        this.factors = a;
    }

    /**
     * Returns the smallest prime number in the factorization.
     */
    public long minPrime() {
        return factors[0].prime();
    }

    /**
     * Returns the largest prime number in the factorizaiton.
     */
    public long maxPrime() {
        return factors[factors.length - 1].prime();
    }

    /**
     * Returns the value that been factorized.
     *
     * @return the value that been factorized
     */
    public long value() {
        long n = 1;
        for (PrimeFactor f : factors)
            n *= f.value();
        return n;
    }

    /**
     * Multiply new factor to construct a new factorization.
     *
     * @param factor new factor to add
     * @return the new factorization
     */
    public Factorization multiply(PrimeFactor factor) {
        long p = factor.prime();
        int len = factors.length;
        PrimeFactor f = null;

        int i;
        for (i = 0; i < len; i++) {
            if (factors[i].prime() >= p) {
                f = factors[i];
                break;
            }
        }

        if (f == null || f.prime() != p)
            len++;

        PrimeFactor[] res = new PrimeFactor[len];
        if (f == null) {
            System.arraycopy(factors, 0, res, 0, len - 1);
            res[len - 1] = factor;
        } else if (f.prime() == p) {
            System.arraycopy(factors, 0, res, 0, len);
            res[i] = new PrimeFactor(p, f.power() + factor.power());
        } else {
            System.arraycopy(factors, 0, res, 0, i);
            System.arraycopy(factors, i, res, i+1, len - i - 1);
            res[i] = factor;
        }
        return new Factorization(res);
    }

    /**
     * Multiply new factor to construct a new factorization.
     *
     * @param p the prime
     * @param a the power
     * @return the new factorization
     */
    public Factorization multiply(long p, int a) {
        return multiply(new PrimeFactor(p, a));
    }

    /**
     * Multiply factors of another factorization to construct a new
     * factorizations. That is, create a factorization of multiples
     * of two numbers.
     *
     * @param other another factorization
     * @return the new factorization
     */
    public Factorization multiply(Factorization other) {
        PrimeFactor[] f1 = this.factors;
        PrimeFactor[] f2 = other.factors;
        int len1 = f1.length;
        int len2 = f2.length;
        int len = 0;

        if (len1 == 0)
            return other;
        if (len2 == 0)
            return this;

        for (int i = 0, j = 0; ;) {
            if (i == len1) {
                len += len2 - j;
                break;
            }
            if (j == len2) {
                len += len1 - i;
                break;
            }

            long p1 = f1[i].prime(), p2 = f2[j].prime();
            if (p1 < p2) {
                i++;
                len++;
            } else if (p2 < p1) {
                j++;
                len++;
            } else {
                i++;
                j++;
                len++;
            }
        }

        PrimeFactor[] res = new PrimeFactor[len];
        int k = 0;

        for (int i = 0, j = 0; ;) {
            if (i == len1) {
                while (j < len2)
                    res[k++] = f2[j++];
                break;
            }
            if (j == len2) {
                while (i < len1)
                    res[k++] = f1[i++];
                break;
            }

            long p1 = f1[i].prime(), p2 = f2[j].prime();
            if (p1 < p2) {
                res[k++] = f1[i];
                i++;
            } else if (p2 < p1) {
                res[k++] = f2[j];
                j++;
            } else {
                res[k++] = new PrimeFactor(p1, f1[i].power() + f2[j].power());
                i++;
                j++;
            }
        }

        return new Factorization(res);
    }

    /**
     * Remove a prime factor from this factorization.
     *
     * @param p the prime number of the factor
     * @param a the power of the factor
     */
    public Factorization remove(long p, int a) {
        int len = factors.length;
        for (PrimeFactor f : factors) {
            if (f.prime() == p) {
                if (f.power() <= a)
                    len--;
                break;
            }
        }

        PrimeFactor[] res = new PrimeFactor[len];
        for (int i = 0, j = 0; i < factors.length; i++) {
            PrimeFactor f = factors[i];
            if (f.prime() == p) {
                if (f.power() > a)
                    res[j++] = new PrimeFactor(p, f.power() - a);
            } else {
                res[j++] = f;
            }
        }
        return new Factorization(res);
    }

    /**
     * Remove a prime factor from this factorization.
     *
     * @param factor the prime factor to be removed
     */
    public Factorization remove(PrimeFactor factor) {
        return remove(factor.prime(), factor.power());
    }

    /**
     * Remove prime factors from this factorization.
     *
     * @param other the prime factors to be removed
     */
    public Factorization remove(Factorization other) {
        PrimeFactor[] f1 = this.factors;
        PrimeFactor[] f2 = other.factors;
        int len1 = f1.length;
        int len2 = f2.length;
        int len = 0;

        if (len1 == 0 || len2 == 0)
            return this;

        for (int i = 0, j = 0; i < len1; ) {
            if (j == len2) {
                len += len1 - i;
                break;
            }

            long p1 = f1[i].prime(), p2 = f2[j].prime();
            if (p1 < p2) {
                i++;
                len++;
            } else if (p2 < p1) {
                j++;
            } else {
                if (f1[i].power() > f2[j].power())
                    len++;
                i++;
                j++;
            }
        }

        PrimeFactor[] res = new PrimeFactor[len];
        int k = 0;

        for (int i = 0, j = 0; i < len1; ) {
            if (j == len2) {
                while (i < len1)
                    res[k++] = f1[i++];
                break;
            }

            long p1 = f1[i].prime(), p2 = f2[j].prime();
            if (p1 < p2) {
                res[k++] = f1[i];
                i++;
            } else if (p2 < p1) {
                j++;
            } else {
                if (f1[i].power() > f2[j].power())
                    res[k++] = new PrimeFactor(p1, f1[i].power() - f2[j].power());
                i++;
                j++;
            }
        }

        return new Factorization(res);
    }

    /**
     * Returns the power of factorization.
     *
     * @param a the exponent
     * @return the power of factorization.
     */
    public Factorization power(int a) {
        PrimeFactor[] res = new PrimeFactor[factors.length];
        for (int i = 0; i < res.length; i++) {
            PrimeFactor f = factors[i];
            res[i] = new PrimeFactor(f.prime(), a * f.power());
        }
        return new Factorization(res);
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
        long r = 1;
        for (PrimeFactor f : factors) {
            long p = f.prime();
            r = r * (p - 1) * pow(p, f.power() - 1);
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
        return factors.length % 2 == 0 ? 1 : -1;
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
        return factors.length;
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
        long n = value();

        if (!isCoprime(a, n))
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
        return Arrays.asList(factors).iterator();
    }

    /**
     * Performs the given action for each element of prime factors of <em>n</em>.
     */
    @Override
    public void forEach(Consumer<? super PrimeFactor> action) {
        for (PrimeFactor f : factors) {
            action.accept(f);
        }
    }

    /**
     * Creates a Spliterator over the elements of prime factors.
     */
    @Override
    public Spliterator<PrimeFactor> spliterator() {
        return Spliterators.spliterator(factors, Spliterator.ORDERED);
    }

    /**
     * Returns the prime factors as an array.
     */
    public PrimeFactor[] factors() {
        return factors.clone();
    }

    /**
     * Returns the String representation of an integer factorization.
     */
    public String toString() {
        return Arrays.toString(factors);
    }
}
