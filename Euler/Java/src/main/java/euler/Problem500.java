package euler;

import java.util.PriorityQueue;

import euler.algo.PrimeCounter;
import euler.algo.PrimeSieve;
import static euler.algo.Library.modmul;

public final class Problem500 {
    private Problem500() {}

    public static long solve(int limit, long moduli) {
        PrimeSieve sieve = new PrimeSieve((int)PrimeCounter.approxPrime(limit));
        PriorityQueue<Long> q = new PriorityQueue<>(limit);
        for (int p = 2, i = 0; i < limit; p = sieve.nextPrime(p), i++)
            q.offer((long)p);

        long product = 1;
        for (int i = 0; i < limit; i++) {
            long p = q.poll();
            q.offer(p * p);
            product = modmul(product, p, moduli);
        }
        return product;
    }

    public static void main(String[] args) {
        System.out.println(solve(500500, 500_500_507));
    }
}
