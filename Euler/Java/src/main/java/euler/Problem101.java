package euler;

import euler.algo.Polynomial;

public final class Problem101 {
    private Problem101() {}

    public static long solve() {
        Polynomial u = Polynomial.make(1, -1, 1, -1, 1, -1, 1, -1, 1, -1, 1);

        long sum = 0;
        for (int k = 1; k <= 10; k++)
            sum += OP(u, k);
        return sum;
    }

    private static long OP(Polynomial u, int n) {
        long[] x = new long[n], y = new long[n];
        for (int i = 0; i < n; i++) {
            x[i] = i + 1;
            y[i] = u.evaluate(i + 1);
        }
        return interpolatingPolynomial(x, y).evaluate(n + 1);
    }

    /**
     * Let x<sub>0</sub>, x<sub>1</sub>, ..., x<sub>n</sub> be distinct numbers,
     * and let f(x) be a function defined on a domain containing these numbers.
     * Given a number x*, the following algorithm computes y* = p<sub>n</sub>(x*),
     * where p<sub>n</sub>(x) is the nth interpolating polynomial of f(x) that
     * interpolates f(x) at the points x<sub>0</sub>, x<sub>1</sub>, ..., x<sub>n</sub>.
     *
     * <pre>
     * for j = 0 to n do
     *     Q[j] = f(x[j])
     * end
     * for j = 1 to n do
     *     for k = n to j do
     *         Q[k] = [(x - x[k])Q[k-1] - (x - x[k-j])Q[k]] / (x[k-j] - x[k])
     *     end
     * end
     * y* = Q[n]
     * </pre>
     */
    private static Polynomial interpolatingPolynomial(long[] x, long[] y) {
        int n = x.length - 1;
        Polynomial[] Q = new Polynomial[n + 1];

        for (int j = 0; j <= n; j++)
            Q[j] = new Polynomial(y[j]);

        for (int j = 1; j <= n; j++) {
            for (int k = n; k >= j; k--) {
                Q[k] = Q[k-1].mul(Polynomial.make(1, -x[k]))
                  .sub(Q[k].mul(Polynomial.make(1, -x[k-j])))
                  .div(x[k-j] - x[k]);
            }
        }

        return Q[n];
    }

    public static void main(String[] args) {
        System.out.println(solve());
    }
}
