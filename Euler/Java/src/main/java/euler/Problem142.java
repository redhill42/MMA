package euler;

import static euler.algo.Library.even;
import static euler.algo.Library.isSquare;

public final class Problem142 {
    private Problem142() {}

    public static int solve() {
        int a, b, c, d, e, f;
        for (int m = 2; ; m++) {
            a = m * m;
            for (int n = 1; n < m; n++) {
                c = n * n;
                f = a - c;
                if (f <= 0 || !isSquare(f))
                    continue;
                for (int k = even(n) ? 2 : 1; k < n; k += 2) {
                    d = k * k;
                    e = a - d;
                    b = c - e;
                    if (b > 0 && e > 0 && isSquare(b) && isSquare(e)) {
                        int x = (d + c) / 2;
                        int y = (e + f) / 2;
                        int z = (c - d) / 2;
                        return x + y + z;
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(solve());
    }
}
