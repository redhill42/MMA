package euler;

public final class Problem6 {
    private Problem6() {}

    private static long sumOfSquare(int n) {
        return n * (n + 1) * (2*n + 1) / 6;
    }

    private static long squareOfSum(int n) {
        long x = n * (n + 1) / 2;
        return x * x;
    }

    public static long solve(int n) {
        return squareOfSum(n) - sumOfSquare(n);
    }

    public static void main(String[] args) {
        int n = 100;
        if (args.length > 0)
            n = Integer.parseInt(args[0]);
        System.out.println(solve(n));
    }
}
