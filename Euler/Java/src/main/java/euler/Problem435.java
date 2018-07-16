package euler;

import euler.algo.Matrix;
import euler.algo.Vector;

public final class Problem435 {
    private Problem435() {}

    public static long solve(long n, int x, long m) {
        long sum = 0;
        for (int i = 1; i <= x; i++)
            sum += F(n, i, m);
        return sum % m;
    }

    private static long F(long n, int x, long m) {
        Matrix A = Matrix.valueOf(new long[][]{{x, x*x, 0}, {1, 0, 0}, {1, 0, 1}});
        Vector V = Vector.valueOf(x, 0, 0);
        return A.modpow(n, m).modmul(V, m).a(2);
    }

    public static void main(String[] args) {
        System.out.println(solve((long)1e15, 100, 1307674368000L));
    }
}
