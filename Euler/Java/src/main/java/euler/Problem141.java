package euler;

public class Problem141 {
    private final static long LIMIT = 1_000_000_000_000L;

    private static boolean isSquare(long n) {
        long r = (long)Math.sqrt(n);
        return r * r == n;
    }

    private static long gcd(long a, long b) {
        while (b != 0) {
            long m = a % b;
            a = b;
            b = m;
        }
        return a;
    }

    public long solve() {
        long sum = 0;
        for (long a = 2; a <= 10000; a++) {
            for (long b = 1; b < a; b++) {
                if (gcd(a, b) != 1)
                    continue;
                for (long c = 1; c < LIMIT; c++) {
                    long n = a*a*a*b*c*c + b*b*c;
                    if (n > LIMIT)
                        break;
                    if (isSquare(n))
                        sum += n;
                }
            }
        }
        return sum;
    }

    public static void main(String[] args) {
        System.out.println(new Problem141().solve());
    }
}
