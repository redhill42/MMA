package euler;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

import euler.algo.PrimeSieve;
import static euler.algo.Library.lcm;
import static euler.algo.Library.modinv;

public final class Problem152 {
    private Problem152() {}

    private static class Solver {
        private final int[] candidates;
        private final long L;

        private final long[] reciprocals;
        private final long[] partials;
        private final Map<Long, Integer> lastNumbers = new HashMap<>();

        private static final int lastNumberThreshold = 18;

        Solver(int limit) {
            candidates = getCandidates(limit);

            // multiply LCM on reciprocal of square to eliminate fractions
            long L = 1;
            for (int n : candidates)
                 L = lcm(L, n * n);
            this.L = L;

            reciprocals = new long[candidates.length];
            for (int i = 0; i < candidates.length; i++) {
                reciprocals[i] = L / (candidates[i] * candidates[i]);
            }

            partials = new long[candidates.length];
            partials[partials.length - 1] = reciprocals[reciprocals.length - 1];
            for (int i = reciprocals.length - 2; i >= 0; i--) {
                partials[i] = partials[i+1] + reciprocals[i];
            }

            if (candidates.length > lastNumberThreshold) {
                int start = candidates.length - lastNumberThreshold;
                for (int mask = 1; mask < (1 << lastNumberThreshold); mask++) {
                    long current = 0;
                    for (int i = 0, m = mask; i < lastNumberThreshold; i++) {
                        if ((m & 1) != 0)
                            current += reciprocals[start + i];
                        m >>= 1;
                    }
                    lastNumbers.compute(current, (k, v) -> v == null ? 1 : v + 1);
                }
            }
        }

        private static int[] getCandidates(int limit) {
            PrimeSieve sieve = new PrimeSieve(limit);
            BitSet candidates = new BitSet(limit + 1);

            candidates.set(2, limit + 1);
            for (int p = 2; p > 0; p = sieve.nextPrime(p)) {
                int q = p;
                while (p * q <= limit)
                    q *= p;
                if (reject(q, limit)) {
                    for (int x = q; x <= limit; x += q)
                        candidates.clear(x);
                }
            }

            int[] result = new int[candidates.cardinality()];
            int i = 0, n = candidates.nextSetBit(0);
            while (n >= 0) {
                result[i++] = n;
                n = candidates.nextSetBit(n+1);
            }
            return result;
        }

        private static boolean reject(int q, int limit) {
            int n = limit / q;
            int mod = q * q;
            int[] inv = new int[n];

            for (int i = 1; i <= n; i++) {
                inv[i-1] = (int)modinv(i * i, mod);
            }

            for (int mask = 1; mask < 1 << n; mask++) {
                int s = 0;
                for (int i = 0, m = mask; i < n; i++) {
                    if ((m & 1) != 0)
                        s += inv[i];
                    m >>= 1;
                }
                if (s % mod == 0) {
                    return false;
                }
            }
            return true;
        }

        private int search(int i, long s, int k) {
            if (s == L / 2)
                return k + 1;

            if (s > L / 2 || s + partials[i] < L / 2)
                return k;

            for (; i < candidates.length; i++) {
                if (i >= candidates.length - lastNumberThreshold)
                    return k + lastNumbers.getOrDefault(L / 2 - s, 0);
                k = search(i + 1, s + reciprocals[i], k);
            }

            return k;
        }

        public int solve() {
            return search(0, 0, 0);
        }
    }

    public static int solve(int limit) {
        return new Solver(limit).solve();
    }

    public static void main(String[] args) {
        int n = 80;
        if (args.length > 0)
            n = Integer.parseInt(args[0]);
        System.out.println(solve(n));
    }
}
