package euler;

import euler.algo.Matrix;
import euler.algo.Vector;

import static euler.algo.Library.modinv;
import static euler.algo.Library.modmul;

public final class Problem624 {
    private Problem624() {}

    public static long solve(long n, long m) {
        Matrix an = Matrix.valueOf(new long[][]{
            {1, 6, 4}, {1, 0, 0}, {0, 1, 0}
        });
        Vector a0 = Vector.valueOf(9, 3, 1);

        Matrix bn = Matrix.valueOf(new long[][]{
            {5, 2, -20, -16}, {1, 0, 0, 0}, {0, 1, 0, 0}, {0, 0, 1, 0}
        });
        Vector b0 = Vector.valueOf(145, 31, 5, 1);

        long a = an.modpow(n - 3, m).moddot(a0, m).a(0);
        long b = bn.modpow(n - 4, m).moddot(b0, m).a(0);
        return modmul(a, modinv(b, m), m);
    }

    public static void main(String[] args) {
        System.out.println(solve((long)1e18, 1_000_000_009));
    }
}
