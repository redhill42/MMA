package euler.algo;

/**
 * Same as {@link Rational} except the numerator and denominator are limited to
 * 64-bit integers. Overflow may be happended for rational operations.
 *
 * <p>Note: this class is used for simple operations such as comparison for rational
 * numbers. For high precision rational operations please use {@link Rational} instead.
 */
@SuppressWarnings("unused")
public class Ratio implements Comparable<Ratio> {
    /**
     * The rational number represents ZERO (0/1).
     */
    public static final Ratio ZERO = new Ratio(0, 1);

    /**
     * The rational number represents ONE (1/1).
     */
    public static final Ratio ONE = new Ratio(1, 1);

    /**
     * The rational number represents HALF (1/2).
     */
    public static final Ratio ONE_HALF = new Ratio(1, 2);

    /**
     * The rational number represents ONE_THIRD (1/3).
     */
    public static final Ratio ONE_THIRD = new Ratio(1, 3);

    /**
     * The rational number represents positive infinity (1/0).
     */
    public static final Ratio POSITIVE_INFINITY = new Ratio(1, 0);

    /**
     * The rational number represents negative infinity (-1/0).
     */
    public static final Ratio NEGATIVE_INFINITY = new Ratio(-1, 0);

    // The numerator and denominator component of this rational number
    private final long numer, denom;

    /**
     * Construct a normalized rational number.
     */
    Ratio(long numer, long denom) {
        this.numer = numer;
        this.denom = denom;
    }

    /**
     * Creates a rational number having the specified numerator and denominator
     * components.
     *
     * @param numer the numerator component of this rational number.
     * @param denom the denominator component of this rational number.
     * @return the rational number
     */
    public static Ratio valueOf(long numer, long denom) {
        return make(numer, denom);
    }

    /**
     * Creates a rational number from the specified numeric value.
     *
     * @param val the numeric value
     * @return the rational number
     */
    public static Ratio valueOf(long val) {
        return make(val, 1);
    }

    /**
     * Creates a rational number from string representation.
     *
     * @param str the String representation of the rational number.
     * @return the rational number.
     */
    public static Ratio valueOf(String str) {
        int sep = str.indexOf('/');
        if (sep >= 0) {
            long numer = Long.parseLong(str.substring(0, sep));
            long denom = Long.parseLong(str.substring(sep+1));
            return make(numer, denom);
        } else {
            return new Ratio(Long.parseLong(str), 1);
        }
    }

    /**
     * Creates a rational number from specified numerator and denominator.
     * Performs necessary normalization.
     */
    private static Ratio make(long numer, long denom) {
        if (denom == 0)
            return (numer >= 0) ? POSITIVE_INFINITY : NEGATIVE_INFINITY;
        if (numer == 0)
            return ZERO;
        if (numer == denom)
            return ONE;

        if (denom < 0) {
            numer = -numer;
            denom = -denom;
        }

        long g = Library.gcd(numer, denom);
        if (g != 1) {
            numer /= g;
            denom /= g;
        }
        return new Ratio(numer, denom);
    }

    /**
     * Returns the numerator of this rational number.
     *
     * @return the numerator of this rational number.
     */
    public long numer() {
        return numer;
    }

    /**
     * Returns the denominator of this rational number.
     *
     * @return the denominator of this rational number.
     */
    public long denom() {
        return denom;
    }

    /**
     * Returns the sum of this rational number with the one specified.
     *
     * @param that the rational number to be added.
     * @return {@code this + that}
     */
    public Ratio add(Ratio that) {
        return make(this.numer * that.denom + this.denom * that.numer,
                    this.denom * that.denom);
    }

    /**
     * Returns the difference between this rational number and the one specified.
     *
     * @param that the rational number to be subtracted.
     * @return {@code this - that}
     */
    public Ratio subtract(Ratio that) {
        return make(this.numer * that.denom - this.denom * that.numer,
                    this.denom * that.denom);
    }

    /**
     * Returns the product of this rational number with the one specified.
     *
     * @param that the rational multiplier.
     * @return {@code this * that}
     */
    public Ratio multiply(Ratio that) {
        return make(this.numer * that.numer, this.denom * that.denom);
    }

    /**
     * Returns this rational number divided by the one specified.
     *
     * @param that the rational number
     * @return {@code this / that}
     */
    public Ratio divide(Ratio that) {
        return make(this.numer * that.denom, this.denom * that.numer);
    }

    /**
     * Returns the reciprocal of this rational number.
     *
     * @return {@code 1 / this}
     */
    public Ratio reciprocal() {
        return make(denom, numer);
    }

    /**
     * Returns the negation of this rational number.
     *
     * @return the negation of this rational number.
     */
    public Ratio negate() {
        return new Ratio(-numer, denom);
    }

    /**
     * Returns the signum of this rational number.
     *
     * @return -1, 0, or 1 as the value of this rational number is negative,
     *         zero, or positive.
     */
    public int signum() {
        return numer < 0 ? -1 : numer == 0 ? 0 : 1;
    }

    /**
     * Returns the absolute value of this rational number.
     *
     * @return {@code abs(this)}
     */
    public Ratio abs() {
        return numer >= 0 ? this : negate();
    }

    /**
     * Compares this rational number with the specified Object for equality.
     *
     * @param obj Objects to which this rational number is to be compared.
     * @return {@code true} if and only if the specified object is a
     *         rational number whose value is numerically equal to this
     *         rational number.
     */
    public boolean equals(Object obj) {
        if (obj instanceof Ratio) {
            Ratio that = (Ratio)obj;
            return this.numer == that.numer && this.denom == that.denom;
        }
        return false;
    }

    /**
     * Returns the hash code for this rational number.
     *
     * @return hash code for this rational number.
     */
    public int hashCode() {
        return Long.hashCode(numer) ^ Long.hashCode(denom);
    }

    /**
     * Returns the String representation of this rational number.
     *
     * @return the String representation of this rational number.
     */
    public String toString() {
        if (denom == 0)
            return numer >= 0 ? "Infinity" : "-Infinity";
        if (denom == 1)
            return Long.toString(numer);
        return Long.toString(numer).concat("/").concat(Long.toString(denom));
    }

    /**
     * Compares this rational number with the specified one.
     *
     * @param that rational number to which this rational number is to be compared.
     * @return -1, 0, or 1 as the rational number is numerically less than, equal
     *         to, or greater than.
     */
    @Override
    public int compareTo(Ratio that) {
        return Long.compare(this.numer * that.denom, that.numer * this.denom);
    }

    /**
     * Convert this to a {@link Rational} object.
     *
     * @return this rational number converted to a {@code Rational}
     */
    public Rational toRational() {
        return Rational.valueOf(numer, denom);
    }

    /**
     * Convert this rational number to a {@code double}. This conversion
     * can lose information about the precision of the rational value.
     *
     * @return this rational number converted to a {@code long}
     */
    public double doubleValue() {
        return (double)numer / denom;
    }
}
