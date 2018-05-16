package euler;

public final class Problem239 {
    private Problem239() {}

    private static double factorial(int n) {
        double r = 1.0;
        while (n > 0)
            r *= n--;
        return r;
    }

    private static double binomial(int n, int k) {
        double r = 1.0;
        if (k > n / 2)
            k = n - k;
        for (int i = 1; i <= k; i++)
            r = r * (n - i + 1) / i;
        return r;
    }

    private static double subfactorial(int n) {
        return Math.floor(factorial(n) / Math.E + 0.5);
    }

    public static double solve(int n, int p, int d) {
        int c = n - p;
        int f = p - d;
        double r = 0;

        for (int i = 0; i <= c; i++)
            r += binomial(c, i) * subfactorial(n - f - i);
        return binomial(p, f) * r / factorial(n);
    }

    public static void main(String[] args) {
        System.out.printf("%.12f%n", solve(100, 25, 22));
    }
}
