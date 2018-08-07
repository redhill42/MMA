package euler;

import euler.algo.PrimeFactor;

import static euler.algo.Library.factorize;
import static euler.algo.Library.nextPrime;
import static euler.algo.Library.pow;

public final class Problem176 {
    private Problem176() {}

    public static long solve(int x) {
        PrimeFactor[] factors = factorize(2 * x + 1).factors();
        long p = 2;
        long res = 1;

        for (int i = factors.length; --i >= 0; ) {
            PrimeFactor f = factors[i];
            for (int j = 0; j < f.power(); j++) {
                res *= pow(p, ((int)f.prime() - 1) / 2);
                p = nextPrime(p);
            }
        }
        return res * 2;
    }

    public static void main(String[] args) {
        int n = 47547;
        if (args.length > 0)
            n = Integer.parseInt(args[0]);
        System.out.println(solve(n));
    }
}
