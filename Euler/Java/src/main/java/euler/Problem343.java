package euler;

public final class Problem343 {
    private Problem343() {}

    public static long solve(int limit) {
        // sieve for max prime factor of k+1
        long[] sieve = new long[limit + 2];
        for (int k = 2; k <= limit+1; k++) {
            if (sieve[k] == 0)
                for (int j = k; j <= limit+1; j += k)
                    sieve[j] = k;
        }

        // sieve for max prime factor of k^2-k+1
        long[] sieve2 = new long[limit + 1];
        for (int k = 1; k <= limit; k++)
            sieve2[k] = (long)k * k - k + 1;
        for (int k = 1; k <= limit; k++) {
            long p = sieve2[k];
            if (p > 1) { // k^2-k+1 is a prime
                for (long j = k; j <= limit; j += p) {
                    int i = (int)j;
                    while (sieve2[i] % p == 0)
                        sieve2[i] /= p;
                    if (sieve[i+1] < p) // reuse the k+1 sieve
                        sieve[i+1] = p;
                }
            }
        }

        long sum = 0;
        for (int k = 1; k <= limit; k++)
            sum += sieve[k+1] - 1;
        return sum;
    }

    public static void main(String[] args) {
        int n = 2_000_000;
        if (args.length > 0)
            n = Integer.parseInt(args[0]);
        System.out.println(solve(n));
    }
}
