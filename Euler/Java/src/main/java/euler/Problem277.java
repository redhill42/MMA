package euler;

public final class Problem277 {
    private Problem277() {}

    private static long decode(long n, char[] sequence) {
        for (int i = sequence.length - 1; i >= 0 && n != 0; i--)
            n = decode(n, sequence[i]);
        return n;
    }

    private static long decode(long n, char c) {
        long a;
        switch (c) {
        case 'D':
            return 3 * n;
        case 'U':
            a = 3 * n - 2;
            return ((a & 3) == 0) ? a >> 2 : 0;
        case 'd':
            a = 3 * n + 1;
            return ((a & 1) == 0) ? a >> 1 : 0;
        default:
            return 0;
        }
    }

    public static long solve(String sequence, long limit) {
        return solve(sequence.toCharArray(), limit);
    }

    private static long solve(char[] sequence, long limit) {
        long a = 0;
        for (int n = 1; a <= limit; n++)
            a = decode(n, sequence);
        return a;
    }

    public static void main(String[] args) {
        System.out.println(solve("UDDDUdddDDUDDddDdDddDDUDDdUUDd", (long)1e15));
    }
}
