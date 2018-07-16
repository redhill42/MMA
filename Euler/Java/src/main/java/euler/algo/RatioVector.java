package euler.algo;

import java.util.Arrays;
import java.util.function.BinaryOperator;
import java.util.function.LongFunction;
import java.util.function.UnaryOperator;

/**
 * Anologoue to {@link Vector} but use rational number as vector elements.
 */
@SuppressWarnings("unused")
public class RatioVector {
    // vector elements
    final Ratio[] _v;

    RatioVector(Ratio[] values) {
        this._v = values;
    }

    /**
     * Create a vector with the specified elements.
     *
     * @param values the vector elements
     * @return the vector having the specified elements.
     */
    public static RatioVector valueOf(Ratio... values) {
        return new RatioVector(values);
    }

    /**
     * Create a vector with the specified elements.
     *
     * @param values the vector elements
     * @return the vector having the specified elements.
     */
    public static RatioVector valueOf(long... values) {
        Ratio[] v = new Ratio[values.length];
        for (int i = 0; i < values.length; i++)
            v[i] = Ratio.valueOf(values[i]);
        return new RatioVector(v);
    }

    /**
     * Convert an integer vector to a rational vector.
     */
    public static RatioVector valueOf(Vector v) {
        return valueOf(v._v);
    }

    /**
     * Create a vector by evaluating f with i ranging from 1 to n.
     *
     * @param n the vector dimension.
     * @param f the procedure to evaluate vector elements.
     * @return the vector having elements by evaluating f.
     */
    public static RatioVector build(int n, LongFunction<Ratio> f) {
        Ratio[] v = new Ratio[n];
        for (int i = 0; i < n; i++)
            v[i] = f.apply(i+1);
        return new RatioVector(v);
    }

    /**
     * Returns the number of elements in the vector.
     *
     * @return the vector dimension.
     */
    public int length() {
        return _v.length;
    }

    /**
     * Returns the vector element at the specified index.
     *
     * @param i the element index.
     * @return the vector element at {@code index}
     */
    public Ratio a(int i) {
        return _v[i];
    }

    /**
     * Returns a vector that is the result of iteration of the given procedure
     * over all elements of the vector.
     */
    public RatioVector map(UnaryOperator<Ratio> f) {
        Ratio[] t = new Ratio[_v.length];
        for (int i = 0; i < t.length; i++)
            t[i] = f.apply(_v[i]);
        return new RatioVector(t);
    }

    /**
     * Returns a vector that is the result of iteration of the given procedure
     * over all elements of this vector and another vector.
     */
    public RatioVector map2(RatioVector other, BinaryOperator<Ratio> f) {
        if (_v.length != other._v.length)
            throw new UnsupportedOperationException("Vector dimension mismatch");
        Ratio[] t = new Ratio[_v.length];
        for (int i = 0; i < t.length; i++)
            t[i] = f.apply(_v[i], other._v[i]);
        return new RatioVector(t);
    }

    /**
     * Returns negation of the vector.
     */
    public RatioVector neg() {
        return map(Ratio::negate);
    }

    /**
     * Returns the sum of vectors.
     */
    public RatioVector add(RatioVector other) {
        return map2(other, Ratio::add);
    }

    /**
     * Returns Ratio vector contains the elements that added with a constant.
     */
    public RatioVector add(Ratio c) {
        return map(x -> x.add(c));
    }

    /**
     * Returns the difference of two vector.
     */
    public RatioVector sub(RatioVector other) {
        return map2(other, Ratio::subtract);
    }

    /**
     * Returns vector that contains the elements that subtracted with a constant.
     */
    public RatioVector sub(Ratio c) {
        return map(x -> x.subtract(c));
    }

    /**
     * Returns the product of a vector and a factor.
     */
    public RatioVector mul(Ratio c) {
        return map(x -> x.multiply(c));
    }

    /**
     * Returns the dot product of two vectors.
     */
    public Ratio mul(RatioVector other) {
        if (length() != other.length())
            throw new UnsupportedOperationException("Vectors have different dimension");

        Ratio[] A = this._v;
        Ratio[] B = other._v;

        Ratio res = Ratio.ZERO;
        for (int i = 0; i < A.length; i++)
            res = res.add(A[i].multiply(B[i]));
        return res;
    }

    /**
     * Returns the dot product of a vector and a matrix.
     */
    public RatioVector mul(RatioMatrix other) {
        int n = other.rows(), m = other.columns();
        if (length() != n)
            throw new UnsupportedOperationException("Vector and matrix have different dimension");

        Ratio[] A = this._v;
        Ratio[][] B = other._v;
        Ratio[] C = new Ratio[m];

        for (int i = 0; i < m; i++) {
            Ratio sum = Ratio.ZERO;
            for (int j = 0; j < n; j++)
                sum = sum.add(A[j].multiply(B[j][i]));
            C[i] = sum;
        }
        return new RatioVector(C);
    }

    /**
     * Returns the array representation of a vector.
     */
    public Ratio[] toArray() {
        return _v.clone();
    }

    /**
     * Compares two vector for equality.
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof RatioVector))
            return false;
        return Arrays.equals(_v, ((RatioVector)obj)._v);
    }

    /**
     * Returns a hash code value for this vector.
     */
    public int hashCode() {
        return Arrays.hashCode(_v);
    }

    /**
     * Returns the String representation of this vector.
     */
    public String toString() {
        return Arrays.toString(_v);
    }
}
