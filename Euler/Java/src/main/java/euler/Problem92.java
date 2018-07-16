package euler;

import java.util.BitSet;
import java.util.function.IntConsumer;

import euler.util.IntArray;

public final class Problem92 {
    private Problem92() {}

    public static int solve(int limit) {
        BitSet   set_1  = new BitSet(limit);
        BitSet   set_89 = new BitSet(limit);
        IntArray chain  = new IntArray();

        for (int n = 1; n < limit; n++) {
            int next = n;
            do {
                if (set_1.get(next)) {
                    next = 1;
                    break;
                } else if (set_89.get(next)) {
                    next = 89;
                    break;
                } else {
                    chain.add(next);
                    next = digitSquareSum(next);
                }
            } while (next != 1 && next != 89);

            BitSet set = next == 1 ? set_1 : set_89;
            chain.forEach((IntConsumer)set::set);
            chain.clear();
        }

        return set_89.cardinality();
    }

    private static int digitSquareSum(int n) {
        int res = 0;
        while (n > 0) {
            int d = n % 10;
            res += d * d;
            n /= 10;
        }
        return res;
    }

    public static void main(String[] args) {
        int n = 10_000_000;
        if (args.length > 0)
            n = Integer.parseInt(args[0]);
        System.out.println(solve(n));
    }
}
