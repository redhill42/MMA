package euler.algo;

import java.util.Arrays;
import java.util.function.LongBinaryOperator;
import java.util.function.LongUnaryOperator;

import static euler.algo.Library.modmul;

/**
 * This class represents an immutable matrix.
 */
@SuppressWarnings("unused")
public class Matrix {
    final long[][] _v;

    Matrix(long[][] v) {
        this._v = v;
    }

    /**
     * Create a matrix with the specified elements.
     */
    public static Matrix valueOf(long[][] data) {
        return new Matrix(data.clone());
    }

    /**
     * Create an n by m matrix by evaluating f with i ranging from 1 to n
     * and j ranging from 1 to m.
     *
     * @param n number of rows in the matrix.
     * @param m number of columns in the matrix.
     * @param f the procedure to evaluate matrix elements.
     * @return the matrix having elements by evaluating f.
     */
    public static Matrix build(int n, int m, LongBinaryOperator f) {
        long[][] v = new long[n][m];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                v[i][j] = f.applyAsLong(i+1, j+1);
        return new Matrix(v);
    }

    /**
     * Create matrix where the diagonal elements are composed of values.
     *
     * @param values matrix diagonal elements.
     * @return matrix composed with diagonal elements.
     */
    public static Matrix diagonal(long[] values) {
        int n = values.length;
        long[][] v = new long[n][n];
        for (int i = 0; i < n; i++)
            v[i][i] = values[i];
        return new Matrix(v);
    }

    /**
     * Create an n by n diagonal matrix where each diagonal element is
     * the given value.
     *
     * @param n the matrix dimension.
     * @param value the matrix diagonal element value.
     * @return matrix composed with diagonal element value.
     */
    public static Matrix scalar(int n, long value) {
        long[][] v = new long[n][n];
        for (int i = 0; i < n; i++)
            v[i][i] = value;
        return new Matrix(v);
    }

    /**
     * Create an n by n identity matrix.
     *
     * @param n the matrix dimension.
     * @return the identity matrix.
     */
    public static Matrix identity(int n) {
        return scalar(n, 1);
    }

    /**
     * Create an n by m constant element matrix.
     *
     * @param n the number of rows in the matrix.
     * @param m the number of columns in the matrix.
     * @param value the matrix constant element value.
     * @return the constant element matrix.
     */
    public static Matrix fill(int n, int m, long value) {
        long[][] v = new long[n][m];
        for (int i = 0; i < n; i++)
            Arrays.fill(v[i], value);
        return new Matrix(v);
    }

    /**
     * Returns the number of rows in the matrix.
     */
    public int rows() {
        return _v.length;
    }

    /**
     * Returns the number of columns in the matrix.
     */
    public int columns() {
        return _v[0].length;
    }

    /**
     * Indicates if this matrix is square.
     */
    public boolean isSquare() {
        return rows() == columns();
    }

    /**
     * Get the element value at the specified index.
     *
     * @param i the row index.
     * @param j the column index.
     * @return the element value.
     */
    public long a(int i, int j) {
        return _v[i][j];
    }

    /**
     * Returns the row vector identified by the specified index in the matrix.
     *
     * @param i the row index.
     * @return the vector for the specified row.
     */
    public Vector row(int i) {
        return new Vector(_v[i]);
    }

    /**
     * Returns the column vector identified by the specified index in the matrix.
     *
     * @param j the column index.
     * @return the vecotr for the specified column.
     */
    public Vector column(int j) {
        if (j < 0 || j >= _v[0].length)
            throw new IndexOutOfBoundsException("Index: " + j);

        int n = _v.length;
        long[] r = new long[n];
        for (int i = 0; i < n; i++)
            r[i] = _v[i][j];
        return new Vector(r);
    }

    /**
     * Returns a matrix that is the result of iteration of the given procedure
     * over all elements of the matrix.
     */
    public Matrix map(LongUnaryOperator f) {
        int n = rows();
        int m = columns();
        long[][] r = new long[n][m];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                r[i][j] = f.applyAsLong(_v[i][j]);
        return new Matrix(r);
    }

    /**
     * Returns a matrix that is the result of iteration of the given procedure
     * over all elements of this and other matrix.
     */
    public Matrix map2(Matrix other, LongBinaryOperator f) {
        int n = rows();
        int m = columns();
        if (n != other.rows() || m != other.columns())
            throw new UnsupportedOperationException("Matrices have different dimensions");

        long[][] r = new long[n][m];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                r[i][j] = f.applyAsLong(_v[i][j], other._v[i][j]);
        return new Matrix(r);
    }

    /**
     * Returns the negation of this matrix.
     */
    public Matrix neg() {
        return map(x -> -x);
    }

    /**
     * Returns the sum of two matrices.
     */
    public Matrix add(Matrix other) {
        return map2(other, Long::sum);
    }

    /**
     * Return the sum of a matrix and a constant.
     */
    public Matrix add(long a) {
        return map(x -> x + a);
    }

    /**
     * Returns the difference between two matrices.
     */
    public Matrix sub(Matrix other) {
        return map2(other, (x, y) -> x - y);
    }

    /**
     * Subtract vector element from a constant.
     */
    public Matrix sub(long a) {
        return map(x -> x - a);
    }

    /**
     * Returns the product of matrices.
     */
    public Matrix mul(Matrix other) {
        return map2(other, (x, y) -> x * y);
    }

    /**
     * Returns the product of a matrix and factor
     */
    public Matrix mul(long a) {
        return map(x -> x * a);
    }

    /**
     * Returns the dot product of two matrices.
     */
    public Matrix dot(Matrix other) {
        int n = rows(), m = columns(), p = other.columns();
        if (m != other.rows())
            throw new UnsupportedOperationException("Matrices have different dimensions");

        long[][] A = this._v;
        long[][] B = other._v;
        long[][] C = new long[n][p];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < p; j++) {
                long sum = 0;
                for (int k = 0; k < m; k++)
                    sum += A[i][k] * B[k][j];
                C[i][j] = sum;
            }
        }
        return new Matrix(C);
    }

    /**
     * Returns the dot product of a matrix and a vector.
     */
    public Vector dot(Vector other) {
        int n = rows(), m = columns();
        if (m != other.length())
            throw new UnsupportedOperationException("Matrix and vector have different dimensions");

        long[][] A = this._v;
        long[] B = other._v;
        long[] C = new long[n];

        for (int i = 0; i < n; i++) {
            long sum = 0;
            for (int j = 0; j < m; j++)
                sum += A[i][j] * B[j];
            C[i] = sum;
        }
        return new Vector(C);
    }

    /**
     * Returns the matrix raised at the specified exponent.
     */
    public Matrix pow(long n) {
        if (!isSquare())
            throw new UnsupportedOperationException("Not a square matrix");
        if (n < 0)
            throw new UnsupportedOperationException("Matrix inverse not implemented");
        if (n == 0)
            return identity(rows());
        n--;

        Matrix A = this, B = this;
        while (n > 0) {
            if ((n & 1) != 0)
                B = A.dot(B);
            A = A.dot(A);
            n >>= 1;
        }
        return B;
    }

    /**
     * Returns modulo of matrix.
     */
    public Matrix mod(long m) {
        return map(x -> x % m);
    }

    /**
     * Returns the modulo dot product of two matrices.
     */
    public Matrix moddot(Matrix other, long mod) {
        int n = rows(), m = columns(), p = other.columns();
        if (m != other.rows())
            throw new UnsupportedOperationException("Matrices have different dimensions");

        long[][] A = this._v;
        long[][] B = other._v;
        long[][] C = new long[n][p];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < p; j++) {
                long sum = 0;
                for (int k = 0; k < m; k++)
                    sum = (sum + modmul(A[i][k], B[k][j], mod)) % mod;
                C[i][j] = sum;
            }
        }
        return new Matrix(C);
    }

    /**
     * Returns the modulo dot product of a Matrix and a Vector.
     */
    public Vector moddot(Vector other, long mod) {
        int n = rows(), m = columns();
        if (m != other.length())
            throw new UnsupportedOperationException("Matrix and vector have different dimensions");

        long[][] A = this._v;
        long[] B = other._v;
        long[] C = new long[n];

        for (int i = 0; i < n; i++) {
            long sum = 0;
            for (int j = 0; j < m; j++)
                sum = (sum + modmul(A[i][j], B[j], mod)) % mod;
            C[i] = sum;
        }
        return new Vector(C);
    }

    /**
     * Returns the modulo matrix power.
     */
    public Matrix modpow(long n, long m) {
        if (!isSquare())
            throw new UnsupportedOperationException("Not a square matrix");
        if (n < 0)
            throw new UnsupportedOperationException("Matrix inverse not implemented");
        if (n == 0)
            return identity(rows());
        n--;

        Matrix A = this, B = this;
        while (n > 0) {
            if ((n & 1) != 0)
                B = A.moddot(B, m);
            A = A.moddot(A, m);
            n >>= 1;
        }
        return B;
    }

    /**
     * Returns the trace of the matrix.
     */
    public long tr() {
        int n = Math.min(rows(), columns());
        long tr = 0;
        for (int i = 0; i < n; i++)
            tr += _v[i][i];
        return tr;
    }

    /**
     * Returns the transpose of the matrix.
     */
    public Matrix transpose() {
        int n = rows(), m = columns();
        long[][] A = this._v;
        long[][] B = new long[m][n];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                B[i][j] = A[j][i];
        return new Matrix(B);
    }

    /**
     * Returns the array representation of a matrix.
     */
    public long[][] toArray() {
        return _v.clone();
    }

    /**
     * Compare two matrix for equality.
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof Matrix))
            return false;

        Matrix other = (Matrix)obj;
        if (rows() != other.rows() || columns() != other.columns())
            return false;
        for (int i = 0; i < _v.length; i++)
            if (!Arrays.equals(_v[i], other._v[i]))
                return false;
        return true;
    }

    /**
     * Returns a hash code value for this matrix.
     */
    public int hashCode() {
        int h = 0;
        for (long[] r : _v)
            h = h * 31 + Arrays.hashCode(r);
        return h;
    }

    /**
     * Returns the String representation of this matrix.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; ; i++) {
            sb.append(Arrays.toString(_v[i]));
            if (i == _v.length - 1)
                return sb.append(']').toString();
            sb.append(',').append(' ');
        }
    }
}
