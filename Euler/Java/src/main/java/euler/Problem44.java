package euler;

public class Problem44 {
    private static long P(int n) {
        return (long)n * (3 * n - 1) / 2;
    }

    private static boolean PQ(long x) {
        int n = (int)((Math.sqrt(24 * x + 1) + 1) / 6);
        return P(n) == x;
    }

    public long solve() {
        for (int n = 3; ; n++) {
            for (int j = 1; j < n / 2; j++) {
                long Pn = P(n), Pj = P(j);
                long diff = Math.abs(Pn - 2*Pj);
                if (PQ(Pn - Pj) && PQ(diff))
                    return diff;
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(new Problem44().solve());
    }
}