package euler;

public class Problem166 {
    private static boolean complete(int n, int a1, int a2, int a3, int a4, int b1, int b2, int b3, int c1) {
        int b4 = a1 + a2 + a3 + a4 - b1 - b2 - b3;
        int c2 = a1 - a4 + b1 - b3 + c1;
        int c3 = a2 + a3 + 2*a4 - b1 - b2 - c1;
        int c4 = b2 + b3 - c1;
        int d1 = a2 + a3 + a4 - b1 - c1;
        int d2 = a3 + 2*a4 - b1 - b2 + b3 - c1;
        int d3 = a1 - a3 - a4 + b1 + b2 - b3 + c1;
        int d4 = -a4 + b1 + c1;
        return b4 >= 0 && b4 <= n &&
               c2 >= 0 && c2 <= n &&
               c3 >= 0 && c3 <= n &&
               c4 >= 0 && c4 <= n &&
               d1 >= 0 && d1 <= n &&
               d2 >= 0 && d2 <= n &&
               d3 >= 0 && d3 <= n &&
               d4 >= 0 && d4 <= n;
    }

    public static int solve(int n) {
        int count = 0;
        for (int a1 = 0; a1 <= n; a1++)
        for (int a2 = 0; a2 <= n; a2++)
        for (int a3 = 0; a3 <= n; a3++)
        for (int a4 = 0; a4 <= n; a4++)
        for (int b1 = 0; b1 <= n; b1++)
        for (int b2 = 0; b2 <= n; b2++)
        for (int b3 = 0; b3 <= n; b3++)
        for (int c1 = 0; c1 <= n; c1++)
            if (complete(n, a1, a2, a3, a4, b1, b2, b3, c1))
                count++;
        return count;
    }

    public int solve() {
        return solve(9);
    }

    public static void main(String[] args) {
        System.out.println(solve(9));
    }
}
