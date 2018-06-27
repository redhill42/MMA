package euler;

import static java.lang.Math.sqrt;

public final class Problem222 {
    private Problem222() {}

    public static long solve(int minRadius, int maxRadius) {
        int D = 2 * maxRadius;
        int r, r0;
        double ans = 0;

        r0 = maxRadius;
        ans += r0;
        for (r = r0 - 2; r >= minRadius; r -= 2) {
            ans += distance(r, r0, D);
            r0 = r;
        }

        r = (r0 == minRadius) ? minRadius + 1 : minRadius;
        for (; r < maxRadius; r += 2) {
            ans += distance(r, r0, D);
            r0 = r;
        }
        ans += r0;

        return Math.round(ans * 1000);
    }

    private static double distance(int r1, int r2, int D) {
        return sqrt(2 * D * (r1 + r2) - D * D);
    }

    public static void main(String[] args) {
        System.out.println(solve(30, 50));
    }
}
