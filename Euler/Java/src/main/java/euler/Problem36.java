package euler;

import euler.util.Palindrome;
import static euler.util.Utils.reverse;

public final class Problem36 {
    private Problem36() {}

    public static long solve(long limit) {
        Palindrome p = new Palindrome();
        long n, s = 0;
        while ((n = p.next()) < limit)
            if (reverse(n, 2) == n)
                s += n;
        return s;
    }

    public static void main(String[] args) {
        int limit = 1_000_000;
        if (args.length != 0)
            limit = Integer.valueOf(args[0]);
        System.out.println(solve(limit));

        Palindrome p = new Palindrome(1, 16);
        for (int i = 0; i < 1000; i++) {
            System.out.print(Long.toHexString(p.next()) + " ");
        }
    }
}
