package euler;

public final class Problem287 {
    private Problem287() {}

    public static long solve(int n) {
        long N = 1L << n;
        return split(0, N-1, N-1, 0, n);
    }

    private static long split(long x1, long y1, long x2, long y2, int n) {
        long x = (x1 + x2 + 1) >> 1;
        long y = (y1 + y2 + 1) >> 1;
        return 1 + encode(x1, y1, x - 1, y, n)
                 + encode(x, y1, x2, y, n)
                 + encode(x1, y - 1, x - 1, y2, n)
                 + encode(x, y - 1, x2, y2, n);
    }

    private static long encode(long x1, long y1, long x2, long y2, int n) {
        if (x1 == x2 && y1 == y2)
            return 2;
        if (bit(x1, y1, n) == bit(x2, y2, n) && bit(x1, y2, n) == bit(x2, y1, n))
            return 2;
        return split(x1, y1, x2, y2, n);
    }

    private static int bit(long x, long y, int n) {
        long R = 1L << n-1;
        return (x-R)*(x-R) + (y-R)*(y-R) <= R*R ? 1 : 0;
    }

    public static void main(String[] args) {
        int n = 24;
        if (args.length > 0)
            n = Integer.parseInt(args[0]);
        System.out.println(solve(n));
    }
}
