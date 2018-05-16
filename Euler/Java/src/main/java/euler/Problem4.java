package euler;

import static euler.algo.Library.isPalindrome;

public final class Problem4 {
    private Problem4() {}

    public static int solve() {
        return solve(1000000);
    }

    public static int solve(int limit) {
        int largest = 0;
        for (int a = 999; a >= 100; a--) {
            int b, db;
            if (a % 11 == 0) {
                b = 999;
                db = 1;
            } else {
                b = 990;
                db = 11;
            }

            while (b >= a) {
                int n = a * b;
                if (n <= largest)
                    break;
                if (n < limit && isPalindrome(n))
                    largest = n;
                b -= db;
            }
        }
        return largest;
    }

    public static void main(String[] args) {
        System.out.println(solve());
    }
}
