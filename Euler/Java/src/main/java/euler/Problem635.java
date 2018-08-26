package euler;

import euler.algo.SegmentedSieve;

import static euler.algo.Library.mod;
import static euler.algo.Library.modinv;
import static euler.algo.Library.modmul;

public final class Problem635 {
    private Problem635() {}

    public static int solve(int n, int m) {
        SegmentedSieve sieve = new SegmentedSieve(n);
        SegmentedSieve.Segment segment = sieve.segment(3);

        long pf1 = 1, pf2 = 1, pf3 = 1;
        long i = 0;
        long sum = 8;

        for (long p; (p = segment.next()) > 0; ) {
            // compute p!, (2p)!, (3p)!
            while (i < p) {
                pf1 = modmul(pf1, i+1, m);
                pf2 = modmul(pf2, (2*i+1)*(2*i+2), m);
                pf3 = modmul(pf3 * (3*i+1), (3*i+2)*(3*i+3), m);
                i++;
            }

            // compute A2(p) + A3(p)
            long invp = modinv(p, m);
            long a2 = modmul(pf2 * modinv(pf1*pf1, m) + 2*(p-1), invp, m);
            long a3 = modmul(pf3 * modinv(pf1*pf2, m) + 3*(p-1), invp, m);
            sum += (a2 + a3) % m;
        }
        return mod(sum, m);
    }

    public static void main(String[] args) {
        System.out.println(solve(100_000_000, 1_000_000_009));
    }
}
