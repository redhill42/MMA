package euler;

import euler.util.LongRangedTask;

import static euler.algo.Library.gcd;
import static euler.algo.Library.isCoprime;

public final class Problem182 {
    private Problem182() {}

    public static long solve(int p, int q) {
        int phi = (p - 1) * (q - 1);

        return LongRangedTask.parallel(2, phi - 1, (from, to) -> {
            long sum = 0;
            for (int e = from; e <= to; e++)
                if (isCoprime(e, phi) && gcd(e-1, p-1) == 2 && gcd(e-1, q-1) == 2)
                    sum += e;
            return sum;
        });
    }

    public static void main(String[] args) {
        int p = 1009, q = 3643;
        if (args.length == 2) {
            p = Integer.parseInt(args[0]);
            q = Integer.parseInt(args[1]);
        }
        System.out.println(solve(p, q));
    }
}
