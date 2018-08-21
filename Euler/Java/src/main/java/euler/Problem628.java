package euler;

public final class Problem628 {
    private Problem628() {}

    public static long solve(int n, int m) {
        long s = n - 1; int k = n - 2;
        while (k > 0)
            s = (s + 1) * k-- % m;
        return ((s + 1) * (n - 3) + 2) % m;
    }

    public static void main(String[] args) {
        System.out.println(solve(100_000_000, 1_008_691_207));
    }
}
