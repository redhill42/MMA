package euler;

public final class Problem190 {
    private Problem190() {}

    public static long solve(int from, int to) {
        long sum = 0;
        for (int m = from; m <= to; m++)
            sum += solve(m);
        return sum;
    }

    private static long solve(int m) {
        double r = 1;
        double k = 2.0 / (m + 1);
        for (int i = 1; i <= m; i++)
            r *= Math.pow(i * k, i);
        return (long)Math.floor(r);
    }

    public static void main(String[] args) {
        System.out.println(solve(2, 15));
    }
}
