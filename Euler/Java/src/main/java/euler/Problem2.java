package euler;

public final class Problem2 {
    private Problem2() {}

    public static long solve(long limit) {
        long a = 2, b = 8, s = 0, t;
        while (a < limit) {
            s += a;
            t = b;
            b = 4 * b + a;
            a = t;
        }
        return s;
    }

    public static void main(String[] args) {
        long limit = 4_000_000;
        if (args.length > 0)
            limit = Long.parseLong(args[0]);
        System.out.println(solve(limit));
    }
}
