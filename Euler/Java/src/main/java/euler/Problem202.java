package euler;

import static euler.algo.Library.factorize;

public final class Problem202 {
    private Problem202() {}

    public static long solve(long n) {
        n = (n + 3) / 2;
        if (n % 3 == 0)
            return 0;

        long sum = 0;
        for (long d : factorize(n).divisors())
            sum += factorize(d).moebius() * ((n/d + 2)/2 - (n/d+2)/3);
        return 2 * sum;
    }

    public static void main(String[] args) {
        long n = 12017639147L;
        if (args.length > 0)
            n = Long.parseLong(args[0]);
        System.out.println(solve(n));
    }
}
