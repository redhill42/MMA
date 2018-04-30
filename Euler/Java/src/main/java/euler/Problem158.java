package euler;

public final class Problem158 {
    private Problem158() {}

    public static long solve(int k) {
        long max = 0, c = 1, p;
        for (int n = 1; n <= k; n++) {
            c = c * (k - n + 1) / n;
            p = c * ((1L << n) - n - 1);
            max = Math.max(max, p);
        }
        return max;
    }

    public static void main(String[] args) {
        System.out.println(solve(26));
    }
}
