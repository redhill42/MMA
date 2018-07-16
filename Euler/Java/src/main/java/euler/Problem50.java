package euler;

import euler.algo.PrimeSieve;

public final class Problem50 {
    private Problem50() {}

    public static int solve(int limit) {
        PrimeSieve sieve = new PrimeSieve(limit);
        int[] primes = sieve.getPrimes();
        int max_prime = 0, max_len = 0;
        int highsum = 0, sum;

        for (int end = 0; highsum < limit; end++) {
            highsum += primes[end];
            sum = highsum;
            for (int start = 0; start < end - max_len; start++) {
                if (sieve.isPrime(sum)) {
                    max_len = end - start;
                    max_prime = sum;
                    break;
                }
                sum -= primes[start];
            }
        }
        return max_prime;
    }

    public static void main(String[] args) {
        int n = 1_000_000;
        if (args.length > 0)
            n = Integer.parseInt(args[0]);
        System.out.println(solve(n));
    }
}
