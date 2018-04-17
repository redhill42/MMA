package euler;

import java.util.ArrayList;
import java.util.Scanner;

public class Problem126 {
    private final int[] count;

    public Problem126() {
        this(20000);
    }

    public Problem126(int limit) {
        count = new int[limit + 1];
        for (int a = 1; cubes(a, a, a) <= limit; a++)
        for (int b = a; cubes(a, b, a) <= limit; b++)
        for (int c = b; cubes(a, b, c) <= limit; c++) {
            long k = cubes(a, b, c);
            long add = 4 * (a + b + c);
            while (k <= limit) {
                count[(int)k]++;
                k += add;
                add += 8;
            }
        }
    }

    private static long cubes(long a, long b, long c) {
        return 2 * (a*b + b*c + c*a);
    }

    public int get(int n) {
        return count[n];
    }

    public int solve() {
        for (int n = 1; ; n++) {
            if (count[n] == 1000)
                return n;
        }
    }

    public static void main(String[] args) {
        ArrayList<Integer> inputs = new ArrayList<>();
        int limit = 0;

        Scanner in = new Scanner(System.in);
        int t = in.nextInt();
        while (--t >= 0) {
            int n = in.nextInt();
            if (n > limit)
                limit = n;
            inputs.add(n);
        }

        Problem126 solver = new Problem126(limit);
        for (int n : inputs) {
            System.out.println(solver.get(n));
        }
    }
}
