package euler.algo;

import java.util.Arrays;
import java.util.function.LongBinaryOperator;
import java.util.function.LongUnaryOperator;

/**
 * This class represents a immutable vector.
 */
@SuppressWarnings("unused")
public class Vector {
    // vector elements
    final long[] _v;

    Vector(long[] values) {
        this._v = values;
    }

    /**
     * Create a vector with the specified elements.
     *
     * @param values the vector elements
     * @return the vector having the specified elements.
     */
    public static Vector valueOf(long... values) {
        return new Vector(values);
    }

    /**
     * Create a vector by evaluating f with i ranging from 1 to n.
     *
     * @param n the vector dimension.
     * @param f the procedure to evaluate vector elements.
     * @return the vector having elements by evaluating f.
     */
    public static Vector build(int n, LongUnaryOperator f) {
        long[] v = new long[n];
        for (int i = 0; i < n; i++)
            v[i] = f.applyAsLong(i+1);
        return new Vector(v);
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
    public long a(int i) {
        return _v[i];
    }

    /**
     * Returns a vector that is the result of iteration of the given procedure
     * over all elements of the vector.
     */
    public Vector map(LongUnaryOperator f) {
        long[] t = new long[_v.length];
        for (int i = 0; i < t.length; i++)
            t[i] = f.applyAsLong(_v[i]);
        return new Vector(t);
    }

    /**
     * Returns a vector that is the result of iteration of the given procedure
     * over all elements of this vector and another vector.
     */
    public Vector map2(Vector other, LongBinaryOperator f) {
        if (_v.length != other._v.length)
            throw new UnsupportedOperationException("Vector dimension mismatch");
        long[] t = new long[_v.length];
        for (int i = 0; i < t.length; i++)
            t[i] = f.applyAsLong(_v[i], other._v[i]);
        return new Vector(t);
    }

    /**
     * Returns negation of the vector.
     */
    public Vector neg() {
        return map(x -> -x);
    }

    /**
     * Returns the sum of vectors.
     */
    public Vector add(Vector other) {
        return map2(other, Long::sum);
    }

    /**
     * Returns vector that contains the elements that added with a constant.
     */
    public Vector add(long a) {
        return map(x -> x + a);
    }

    /**
     * Returns the difference of two vectors.
     */
    public Vector sub(Vector other) {
        return map2(other, (x, y) -> x - y);
    }

    /**
     * Returns vector that contains the elements that subtracted with a constant.
     */
    public Vector sub(long a) {
        return map(x -> x - a);
    }

    /**
     * Return the product of a vector and a factor.
     */
    public Vector mul(long a) {
        return map(x -> x * a);
    }

    /**
     * Returns the dot product of two vectors.
     */
    public long mul(Vector other) {
        if (length() != other.length())
            throw new UnsupportedOperationException("Vectors have different dimension");

        long[] A = this._v;
        long[] B = other._v;

        long res = 0;
        for (int i = 0; i < A.length; i++)
            res += A[i] * B[i];
        return res;
    }

    /**
     * Returns the dot product of a vector and a matrix.
     */
    public Vector mul(Matrix other) {
        int n = other.rows(), m = other.columns();
        if (length() != n)
            throw new UnsupportedOperationException("Vector and matrix have differnet dimension");

        long[] A = this._v;
        long[][] B = other._v;
        long[] C = new long[m];

        for (int i = 0; i < m; i++) {
            long sum = 0;
            for (int j = 0; j < n; j++)
                sum += A[j] * B[j][i];
            C[i] = sum;
        }
        return new Vector(C);
    }

    /**
     * Returns the modulo of vector.
     */
    public Vector mod(long m) {
        return map(x -> x % m);
    }

    /**
     * Returns the modulo dot product of two vectors.
     */
    public long modmul(Vector other, long M) {
        if (length() != other.length())
            throw new UnsupportedOperationException("Vectors have different dimension");

        long[] A = this._v;
        long[] B = other._v;

        long res = 0;
        for (int i = 0; i < A.length; i++)
            res = (res + Library.modmul(A[i], B[i], M)) % M;
        return res;
    }

    /**
     * Returns the modulo dot product of a vector and a matrix.
     */
    public Vector modmul(Matrix other, long M) {
        int n = other.rows(), m = other.columns();
        if (length() != n)
            throw new UnsupportedOperationException("Vector and matrix have different dimension");

        long[] A = this._v;
        long[][] B = other._v;
        long[] C = new long[m];

        for (int i = 0; i < m; i++) {
            long sum = 0;
            for (int j = 0; j < n; j++)
                sum = (sum + Library.modmul(A[j], B[j][i], M)) % M;
            C[i] = sum;
        }
        return new Vector(C);
    }

    /**
     * Returns the array representation of a vector.
     */
    public long[] toArray() {
        return _v.clone();
    }

    /**
     * Compares two vector for equality.
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof Vector))
            return false;
        return Arrays.equals(_v, ((Vector)obj)._v);
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
