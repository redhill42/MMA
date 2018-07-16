package euler.algo;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

import static euler.algo.Library.even;

/**
 * Anologoue to {@link Matrix} but use rational number as matrix elements.
 */
@SuppressWarnings("unused")
public class RatioMatrix {
    final Ratio[][] _v;

    RatioMatrix(Ratio[][] v) {
        this._v = v;
    }

    /**
     * Create a matrix with the specified elements.
     */
    public static RatioMatrix valueOf(Ratio[][] values) {
        return new RatioMatrix(values.clone());
    }

    /**
     * Create a matrix with the specified elements.
     */
    public static RatioMatrix valueOf(long[][] values) {
        Ratio[][] v = new Ratio[values.length][];
        for (int i = 0; i < v.length; i++) {
            v[i] = new Ratio[values[i].length];
            for (int j = 0; j < v[i].length; j++)
                v[i][j] = Ratio.valueOf(values[i][j]);
        }
        return new RatioMatrix(v);
    }

    /**
     * Convert an integer matrix to a rational matrix.
     */
    public static RatioMatrix valueOf(Matrix m) {
        return valueOf(m._v);
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
    public static RatioMatrix build(int n, int m, BiFunction<Integer,Integer,Ratio> f) {
        Ratio[][] v = new Ratio[n][m];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                v[i][j] = f.apply(i+1, j+1);
        return new RatioMatrix(v);
    }

    /**
     * Create matrix where the diagonal elements are composed of values.
     *
     * @param values matrix diagonal elements.
     * @return matrix composed with diagonal elements.
     */
    public static RatioMatrix diagonal(Ratio[] values) {
        int n = values.length;
        Ratio[][] v = new Ratio[n][n];
        for (int i = 0; i < n; i++) {
            Arrays.fill(v[i], Ratio.ZERO);
            v[i][i] = values[i];
        }
        return new RatioMatrix(v);
    }

    /**
     * Create an n by n diagonal matrix where each diagonal element is
     * the given value.
     *
     * @param n the matrix dimension.
     * @param value the matrix diagonal element value.
     * @return matrix composed with diagonal element value.
     */
    public static RatioMatrix scalar(int n, Ratio value) {
        Ratio[][] v = new Ratio[n][n];
        for (int i = 0; i < n; i++) {
            Arrays.fill(v[i], Ratio.ZERO);
            v[i][i] = value;
        }
        return new RatioMatrix(v);
    }

    /**
     * Create an n by n identity matrix.
     *
     * @param n the matrix dimension.
     * @return the identity matrix.
     */
    public static RatioMatrix identity(int n) {
        return scalar(n, Ratio.ONE);
    }

    /**
     * Create an n by m constant element matrix.
     *
     * @param n the number of rows in the matrix.
     * @param m the number of columns in the matrix.
     * @param value the matrix constant element value.
     * @return the constant element matrix.
     */
    public static RatioMatrix fill(int n, int m, Ratio value) {
        Ratio[][] v = new Ratio[n][m];
        for (int i = 0; i < n; i++)
            Arrays.fill(v[i], value);
        return new RatioMatrix(v);
    }

    /**
     * Returns the number rows in the matrix.
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
    public Ratio a(int i, int j) {
        return _v[i][j];
    }

    /**
     * Returns the row vector identified by the specified index in the matrix.
     *
     * @param i the row index.
     * @return the vector for the specified row.
     */
    public RatioVector row(int i) {
        return new RatioVector(_v[i]);
    }

    /**
     * Returns the column vector identified by the specified index in the matrix.
     *
     * @param j the column index.
     * @return the vecotr for the specified column.
     */
    public RatioVector column(int j) {
        if (j < 0 || j >= _v[0].length)
            throw new IndexOutOfBoundsException("Index: " + j);

        int n = _v.length;
        Ratio[] r = new Ratio[n];
        for (int i = 0; i < n; i++)
            r[i] = _v[i][j];
        return new RatioVector(r);
    }

    /**
     * Returns a matrix that is the result of iteration of the given procedure
     * over all elements of the matrix.
     */
    public RatioMatrix map(UnaryOperator<Ratio> f) {
        int n = rows(), m = columns();
        Ratio[][] r = new Ratio[n][m];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                r[i][j] = f.apply(_v[i][j]);
        return new RatioMatrix(r);
    }

    /**
     * Returns a matrix that is the result of iteration of the given procedure
     * over all elements of this and other matrix.
     */
    public RatioMatrix map2(RatioMatrix other, BinaryOperator<Ratio> f) {
        int n = rows(), m = columns();
        if (n != other.rows() || m != other.columns())
            throw new UnsupportedOperationException("Matrices have different dimensions");

        Ratio[][] r = new Ratio[n][m];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                r[i][j] = f.apply(_v[i][j], other._v[i][j]);
        return new RatioMatrix(r);
    }

    /**
     * Returns the negation of this matrix.
     */
    public RatioMatrix neg() {
        return map(Ratio::negate);
    }

    /**
     * Returns the sum of two matrices.
     */
    public RatioMatrix add(RatioMatrix other) {
        return map2(other, Ratio::add);
    }

    /**
     * Returns the sum of a matrix and a constant.
     */
    public RatioMatrix add(Ratio c) {
        return map(x -> x.add(c));
    }

    /**
     * Returns the difference between two matrics.
     */
    public RatioMatrix sub(RatioMatrix other) {
        return map2(other, Ratio::subtract);
    }

    /**
     * Subtract matrix element from a constant.
     */
    public RatioMatrix sub(Ratio c) {
        return map(x -> x.subtract(c));
    }

    /**
     * Returns the product of a matrix and a factor.
     */
    public RatioMatrix mul(Ratio c) {
        return map(x -> x.multiply(c));
    }

    /**
     * Returns the dot product of two matrices.
     */
    public RatioMatrix mul(RatioMatrix other) {
        int n = rows(), m = columns(), p = other.columns();
        if (m != other.rows())
            throw new UnsupportedOperationException("Matrices have different dimensions");

        Ratio[][] A = this._v;
        Ratio[][] B = other._v;
        Ratio[][] C = new Ratio[n][p];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < p; j++) {
                Ratio sum = Ratio.ZERO;
                for (int k = 0; k < m; k++)
                    sum = sum.add(A[i][k].multiply(B[k][j]));
                C[i][j] = sum;
            }
        }
        return new RatioMatrix(C);
    }

    /**
     * Returns the dot product of a matrix and a vector.
     */
    public RatioVector mul(RatioVector other) {
        int n = rows(), m = columns();
        if (m != other.length())
            throw new UnsupportedOperationException("Matrix and vector have different dimensions");

        Ratio[][] A = this._v;
        Ratio[] B = other._v;
        Ratio[] C = new Ratio[n];

        for (int i = 0; i < n; i++) {
            Ratio sum = Ratio.ZERO;
            for (int j = 0; j < m; j++)
                sum = sum.add(A[i][j].multiply(B[j]));
            C[i] = sum;
        }
        return new RatioVector(C);
    }

    /**
     * Returns the division of two matrices.
     */
    public RatioMatrix div(RatioMatrix other) {
        return mul(other.inverse());
    }

    /**
     * Returns the division of the matrix and a constant.
     */
    public RatioMatrix div(Ratio c) {
        return map(x -> x.divide(c));
    }

    /**
     * Returns the matrix raised at the specified exponent.
     */
    public RatioMatrix pow(long n) {
        if (!isSquare())
            throw new UnsupportedOperationException("Not a square matrix");
        if (n == 0)
            return identity(rows());
        if (n < 0)
            return pow(-n).inverse();
        n--;

        RatioMatrix A = this, B = this;
        while (n > 0) {
            if ((n & 1) != 0)
                B = A.mul(B);
            A = A.mul(A);
            n >>= 1;
        }
        return B;
    }

    /**
     * LU decomposition.
     *
     * INPUT: A - array of elements of a square matrix having dimension N
     * OUTPUT: Matrix A is changed, it contains both matrices L-E and U as
     *         A=(L-E)+U such that P*A=L*U.
     *         The permutation matrix is not stored as a matrix, but in an
     *         integer vector P of size N+1 containining column indexes where
     *         the permutation matrix has "1". The last element P[N]=S+N,
     *         where S is the number of row exchanges needed for determinant
     *         computation, det(P)=(-1)^S.
     */
    private static boolean LUPDecompose(Ratio[][] A, int[] P) {
        int n = A.length;
        int i, j, k, imax;
        Ratio maxA, absA;

        for (i = 0; i <= n; i++)
            P[i] = i; // Unit permutation matrix, P[N] initialized with N

        for (i = 0; i < n; i++) {
            maxA = Ratio.ZERO;
            imax = i;

            for (k = i; k < n; k++) {
                absA = A[k][k].abs();
                if (absA.compareTo(maxA) > 0) {
                    maxA = absA;
                    imax = k;
                }
            }

            if (maxA.signum() == 0)
                return false; // failure, matrix is degenerate

            if (imax != i) {
                // pivoting P
                j = P[i];
                P[i] = P[imax];
                P[imax] = j;

                // pivoting rows of A
                Ratio[] ptr = A[i];
                A[i] = A[imax];
                A[imax] = ptr;

                // counting pivots starting from N (for determinant)
                P[n]++;
            }

            for (j = i + 1; j < n; j++) {
                A[j][i] = A[j][i].divide(A[i][i]);

                for (k = i + 1; k < n; k++)
                    A[j][k] = A[j][k].subtract(A[j][i].multiply(A[i][k]));
            }
        }

        return true; // decomposition done
    }

    /**
     * Solve the linear equation this*X=b.
     */
    public RatioVector solve(RatioVector b) {
        if (!isSquare() && rows() != b.length())
            throw new UnsupportedOperationException("Matrix and vector have different dimension");

        int n = rows();
        int[] P = new int[n + 1];
        Ratio[][] A = toArray();
        Ratio[] B = b._v;
        Ratio[] x = new Ratio[n];

        if (!LUPDecompose(A, P))
            return null;

        for (int i = 0; i < n; i++) {
            x[i] = B[P[i]];
            for (int k = 0; k < i; k++)
                x[i] = x[i].subtract(A[i][k].multiply(x[k]));
        }

        for (int i = n - 1; i >= 0; i--) {
            for (int k = i + 1; k < n; k++)
                x[i] = x[i].subtract(A[i][k].multiply(x[k]));
            x[i] = x[i].divide(A[i][i]);
        }

        return new RatioVector(x);
    }

    /**
     * Returns the inverse of this matrix.
     */
    public RatioMatrix inverse() {
        if (!isSquare())
            throw new UnsupportedOperationException("Not a square matrix");

        int n = rows();
        int[] P = new int[n + 1];
        Ratio[][] A = toArray();
        Ratio[][] B = new Ratio[n][n];

        if (!LUPDecompose(A, P))
            throw new IllegalArgumentException("Inverse of matrix is singular");

        for (int j = 0; j < n; j++) {
            for (int i = 0; i < n; i++) {
                if (P[i] == j)
                    B[i][j] = Ratio.ONE;
                else
                    B[i][j] = Ratio.ZERO;
                for (int k = 0; k < i; k++)
                    B[i][j] = B[i][j].subtract(A[i][k].multiply(B[k][j]));
            }

            for (int i = n - 1; i >= 0; i--) {
                for (int k = i + 1; k < n; k++)
                    B[i][j] = B[i][j].subtract(A[i][k].multiply(B[k][j]));
                B[i][j] = B[i][j].divide(A[i][i]);
            }
        }

        return new RatioMatrix(B);
    }

    /**
     * Returns the determinant of the matrix.
     */
    public Ratio det() {
        if (!isSquare())
            throw new UnsupportedOperationException("Not a square matrix");

        int n = rows();
        int[] P = new int[n + 1];
        Ratio[][] A = toArray();

        if (!LUPDecompose(A, P))
            return Ratio.ZERO;

        Ratio det = A[0][0];
        for (int i = 1; i < n; i++)
            det = det.multiply(A[i][i]);
        return even(P[n] - n) ? det : det.negate();
    }

    /**
     * Returns the cofactor of an element in the matrix. It is the value
     * obtained by evaluating the determinant formed by the elements not
     * in that particular row or column.
     *
     * @param row the row index
     * @param col the column index
     * @return the cofactor
     */
    public Ratio cofactor(int row, int col) {
        if (!isSquare())
            throw new UnsupportedOperationException("Not a square matrix");

        int n = rows();
        Ratio[][] A = this._v;
        Ratio[][] M = new Ratio[n-1][n-1];

        int k1 = 0;
        for (int i = 0; i < n; i++) {
            if (i == row)
                continue;

            int k2 = 0;
            for (int j = 0; j < n; j++) {
                if (j == col)
                    continue;
                M[k1][k2++] = A[i][j];
            }
            k1++;
        }

        Ratio det = new RatioMatrix(M).det();
        return even(row + col) ? det : det.negate();
    }

    /**
     * Returns the trace of the matrix.
     */
    public Ratio tr() {
        int n = Math.min(rows(), columns());
        Ratio tr = Ratio.ZERO;
        for (int i = 0; i < n; i++)
            tr = tr.add(_v[i][i]);
        return tr;
    }

    /**
     * Returns the transpose of the matrix.
     */
    public RatioMatrix transpose() {
        int n = rows(), m = columns();
        Ratio[][] A = this._v;
        Ratio[][] B = new Ratio[m][n];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                B[i][j] = A[j][i];
        return new RatioMatrix(B);
    }

    /**
     * Returns the array representation of a matrix.
     */
    public Ratio[][] toArray() {
        Ratio[][] r = new Ratio[_v.length][];
        for (int i = 0; i < r.length; i++)
            r[i] = _v[i].clone();
        return r;
    }

    /**
     * Compare two matrix for equality.
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof RatioMatrix))
            return false;

        RatioMatrix other = (RatioMatrix)obj;
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
        for (Ratio[] r : _v)
            h = h * 31 + Arrays.hashCode(r);
        return h;
    }

    /**
     * Returns the String representation of this matrix.
     */
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append('[');
        for (int i = 0; ; i++) {
            b.append(Arrays.toString(_v[i]));
            if (i == _v.length - 1)
                return b.append(']').toString();
            b.append(',').append(' ');
        }
    }
}
