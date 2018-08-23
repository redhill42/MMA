package euler;

import euler.algo.PrimeSieve;

import static euler.algo.Library.nextPrime;

public final class Problem633 {
    private Problem633() {}

    public static double solve(int maxk) {
        int N = 1;
        for (int i = 1, p = 2; i <= maxk; i++) {
            N *= p;
            p = (int)nextPrime(p);
        }
        PrimeSieve sieve = new PrimeSieve(N * 2);

        double[] d = new double[maxk + 1];
        double[] s = new double[maxk + 1];
        d[0] = 1; s[0] = 0;

        for (int k = 1; k <= maxk; k++) {
            double sk = 0;
            for (int p = 2; p > 0; p = sieve.nextPrime(p))
                sk += 1 / Math.pow((double)p * p - 1, k);
            s[k] = sk;

            double dk = 0;
            for (int i = 1; i <= k; i++)
                dk += s[i] * d[k - i] * Math.pow(-1, i - 1);
            d[k] = dk / k;
        }

        return d[maxk] * 6 / (Math.PI * Math.PI);
    }

    public static void main(String[] args) {
        System.out.printf("%.4e%n", solve(7));
    }
}
