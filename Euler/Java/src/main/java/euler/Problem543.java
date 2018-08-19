package euler;

import euler.algo.PrimeCounter;

import static euler.algo.Library.fibonacci;

public final class Problem543 {
    private Problem543() {}

    public static long solve(int from, int to) {
        PrimeCounter pi = new PrimeCounter(fibonacci(to));

        long sum = 0;
        for (int k = from; k <= to; k++)
            sum += S(pi, fibonacci(k));
        return sum;
    }

    private static long S(PrimeCounter pi, long n) {
        long sum = 0;
        if (n >= 2)
            sum += pi.countPrimes(n);
        if (n >= 4)
            sum += pi.countPrimes(n - 2) + n / 2 - 2;
        if (n >= 6)
            sum += (n - n/2 - 2) * (n/2 - 2);
        return sum;
    }

    public static void main(String[] args) {
        System.out.println(solve(3, 44));
    }
}
