package euler.algo;

import java.util.Arrays;
import java.util.function.LongBinaryOperator;

import static euler.algo.Library.isqrt;
import static java.lang.Math.abs;
import static java.lang.Math.floor;

public class ContinuedFraction {
    private final int[] terms;
    private final int period;

    public ContinuedFraction(int[] terms, int period) {
        this.terms = terms;
        this.period = period;
    }

    public boolean isPeriodic() {
        return period > 0;
    }

    public int getPeriod() {
        return period;
    }

    public int term(int i) {
        if (i < terms.length)
            return terms[i];
        if (!isPeriodic())
            throw new ArrayIndexOutOfBoundsException();

        int k = terms.length - period;
        return terms[k + (i - k) % period];
    }

    public long convergents(LongBinaryOperator op) {
        return convergents(Integer.MAX_VALUE, op);
    }

    public long convergents(int n, LongBinaryOperator op) {
        if (!isPeriodic() && n > terms.length)
            n = terms.length;

        long p0 = 0, q0 = 1;
        long p1 = 1, q1 = 0;
        long r = 0;

        for (int i = 0; i < n && r != -1; i++) {
            int a = term(i);
            long p = a * p1 + p0;
            long q = a * q1 + q0;
            r = op.applyAsLong(p, q);
            p0 = p1; q0 = q1;
            p1 = p;  q1 = q;
        }
        return r;
    }

    public void convergent(int n, long[] r) {
        convergents(n, (p, q) -> {
            r[0] = p;
            r[1] = q;
            return -1;
        });
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();
        int i;

        buf.append("[");
        buf.append(terms[0]);
        if (terms.length > 1)
            buf.append(";");

        for (i = 1; i < terms.length - period; i++) {
            buf.append(terms[i]);
            if (i < terms.length - 1)
                buf.append(",");
        }

        if (i < terms.length) {
            buf.append("{");
            for (; i < terms.length; i++) {
                buf.append(terms[i]);
                if (i < terms.length - 1)
                    buf.append(",");
            }
            buf.append("}");
        }

        buf.append("]");
        return buf.toString();
    }

    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof ContinuedFraction))
            return false;
        ContinuedFraction other = (ContinuedFraction)obj;
        return period == other.period && Arrays.equals(terms, other.terms);
    }

    public int hashCode() {
        return period ^ Arrays.hashCode(terms);
    }

    public static ContinuedFraction rational(int a, int b) {
        int[] terms = new int[8];
        int k = 0;
        int t;

        while (b != 0) {
            terms = expand(terms, k);
            terms[k++] = a / b;
            t = a % b;
            a = b;
            b = t;
        }
        return new ContinuedFraction(Arrays.copyOf(terms, k), 0);
    }

    private static final double EPS = 1e-9;

    public static ContinuedFraction real(double x, int n) {
        int[] terms = new int[n];
        int k = 0;

        while (k < n) {
            int a = (int)floor(x);
            terms = expand(terms, k);
            terms[k++] = a;
            if (abs(x - a) < EPS)
                break;
            x = 1.0 / (x - a);
        }

        if (k != n)
            terms = Arrays.copyOf(terms, k);
        return new ContinuedFraction(terms, 0);
    }

    public static ContinuedFraction sqrt(int n) {
        int a0 = isqrt(n);
        if (a0 * a0 == n)
            return new ContinuedFraction(new int[]{a0}, 0);

        int[] terms = new int[16];
        int P = 0, Q = 1;
        int a;
        int k = 0;

        do {
            a = (a0 + P) / Q;
            P = a * Q - P;
            Q = (n - P * P) / Q;
            terms = expand(terms, k);
            terms[k++] = a;
        } while (a != a0 * 2);
        return new ContinuedFraction(Arrays.copyOf(terms, k), k - 1);
    }

    private static int[] expand(int[] terms, int len) {
        if (terms.length == len) {
            int[] tmp = new int[len * 2];
            System.arraycopy(terms, 0, tmp, 0, len);
            terms = tmp;
        }
        return terms;
    }
}
