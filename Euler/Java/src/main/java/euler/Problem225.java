package euler;

public final class Problem225 {
    private Problem225() {}

    public static int solve(int count) {
        for (int n = 3; ; n += 2) {
            if (!divisible(n) && --count == 0)
                return n;
        }
    }

    private static boolean divisible(int n) {
        int a = 1, b = 1, c = 1;
        do {
            int d = (a + b + c) % n;
            if (d == 0)
                return true;
            a = b; b = c; c = d;
        } while (!(a == 1 && b == 1 && c == 1));
        return false;
    }

    public static void main(String[] args) {
        System.out.println(solve(124));
    }
}
