package euler;

public final class Problem1 {
    private Problem1() {}

    private static int s(int n, int k) {
        int p = (n - 1) / k;
        return k * p * (p + 1) / 2;
    }

    public static int solve(int n, int a, int b) {
        return s(n, a) + s(n, b) - s(n, a * b);
    }

    public static void main(String[] args) {
        System.out.println(solve(1000, 3, 5));
    }
}
