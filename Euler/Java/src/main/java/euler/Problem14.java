package euler;

import java.util.Scanner;

public final class Problem14 {
    private Problem14() {}

    public static int solve(int limit) {
        int max_len = 0, max_n = 0;
        for (int n = 1; n <= limit; n++) {
            long chain = n;
            int k = 1;
            while (chain != 1) {
                ++k;
                chain = (chain % 2 == 0) ? chain / 2 : 3 * chain + 1;
            }
            if (k >= max_len) {
                max_len = k;
                max_n = n;
            }
        }
        return max_n;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int t = in.nextInt();
        while (--t >= 0) {
            int n = in.nextInt();
            System.out.println(solve(n));
        }
    }
}
