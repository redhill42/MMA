package euler;

import euler.algo.Matrix;
import euler.algo.Vector;
import euler.util.IntArray;

public final class Problem577 {
    private Problem577() {}

    @SuppressWarnings("unused")
    public static IntArray bruteForce(int n) {
        IntArray sol = new IntArray();
        for (int i = 3; i <= n; i++)
            sol.add(H(i));
        return sol;
    }

    private static int H(int n) {
        int s = 0;
        for (int x1 = 0; x1 <= n; x1++)
        for (int y1 = 0; y1 <= n - x1; y1++)
        for (int x2 = 0; x2 <= n; x2++)
        for (int y2 = 0; y2 <= n - x2; y2++)
            if (inside(n, x1, y1, x2, y2))
                s++;
        return s / 6;
    }

    private static boolean inside(int n, int x1, int y1, int x2, int y2) {
        if (x1 == x2 && y1 == y2)
            return false;

        Matrix m = Matrix.valueOf(new long[][]{{0, -1}, {1, 1}});
        Vector p1 = Vector.valueOf(x1, y1);
        Vector p2 = Vector.valueOf(x2, y2);
        Vector p3 = p2.add(m.mul(p2.sub(p1)));
        Vector p4 = p3.add(m.mul(p3.sub(p2)));
        Vector p5 = p4.add(m.mul(p4.sub(p3)));
        Vector p6 = p5.add(m.mul(p5.sub(p4)));
        return inside(n, p3) && inside(n, p4) && inside(n, p5) && inside(n, p6);
    }

    private static boolean inside(int n, Vector p) {
        long x = p.a(0), y = p.a(1);
        return x >= 0 && y >= 0 && x + y <= n;
    }

    public static long solve(int n) {
        long sum = 0;
        for (long k = 3; k <= n; k += 3)
            sum += k * (n-k+1) * (n-k+2) * (n-k+3) / 18;
        return sum;
    }

    public static void main(String[] args) {
        int n = 12345;
        if (args.length > 0)
            n = Integer.parseInt(args[0]);
        System.out.println(solve(n));
    }
}
