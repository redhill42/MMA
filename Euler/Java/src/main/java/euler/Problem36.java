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
        long limit = 1_000_000;
        if (args.length != 0)
            limit = Long.valueOf(args[0]);
        System.out.println(solve(limit));
    }
}
