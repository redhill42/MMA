package euler;

public final class Problem510 {
    private Problem510() {}

    private static long search(int a, int b, long limit) {
        int c = a + b;

        long ra = (long)b*b * c*c;
        long rb = (long)a*a * c*c;
        long rc = (long)a*a * b*b;

        if (rb > limit)
            return 0;
        return sum(ra, rb, rc, limit)
             + search(c, a, limit)
             + search(c, b, limit);
    }

    private static long sum(long a, long b, long c, long limit) {
        long n = limit / b;
        return (a + b + c) * n * (n + 1) / 2;
    }

    public static long solve(long limit) {
        return sum(4, 4, 1, limit) + search(2, 1, limit);
    }

    public static void main(String[] args) {
        long n = (long)1e9;
        if (args.length > 0)
            n = Long.parseLong(args[0]);
        System.out.println(solve(n));
    }
}
