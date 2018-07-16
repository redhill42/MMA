package euler;

import static euler.algo.Library.isqrt;

public final class Problem45 {
    private Problem45() {}

    // P(n) = n(3n-1)/2 = P(n-1) + 3n - 2
    // for n = (sqrt(8x+1)-1)/2, if n is an integer then it is a triangular
    // for n = (sqrt(8x+1)+1)/4, if n is an integer then it is a hexagonal
    public static long solve() {
        int  n = 286;
        long p = (long)n * (3*n - 1) / 2;

        for (;;) {
            long t = 8*p + 1, x = isqrt(t);
            if (x*x == t && ((x - 1) & 1) == 0 && ((x + 1) & 3) == 0)
                break;
            p += 3 * ++n - 2;
        }
        return p;
    }

    public static void main(String[] args) {
        System.out.println(solve());
    }
}
