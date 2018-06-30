package euler;

import java.util.HashMap;
import java.util.Map;

import euler.algo.PrimeCounter;
import euler.algo.SegmentedSieve;
import euler.util.IntArray;

import static euler.algo.Library.modmul;

public final class Problem609 {
    private Problem609() {}

    @SuppressWarnings("serial")
    private static class Counter extends HashMap<Integer, int[]> {
        public void add(int n, int a) {
            computeIfAbsent(n, x -> new int[1])[0] += a;
        }

        public void add(IntArray sequence, int pi_gap, int p_gap) {
            if (pi_gap <= 0)
                return;

            for (int i = 0; i < sequence.length; i++) {
                if (sequence.a[i] != 0)
                    add(i, sequence.a[i] * pi_gap);
            }

            if (p_gap > pi_gap) {
                for (int i = 0; i < sequence.length; i++)
                    if (sequence.a[i] != 0)
                        add(i+1, sequence.a[i] * (p_gap - pi_gap));
            }
        }

        public long getResult(long modulo) {
            return values().stream()
                           .mapToLong(x -> x[0])
                           .reduce(1, (x, y) -> modmul(x, y, modulo));
        }
    }

    @SuppressWarnings("ConfusingElseBranch")
    public static long solve(int limit, long modulo) {
        SegmentedSieve sieve = new SegmentedSieve(limit);
        int max_cache = (int)PrimeCounter.Li(limit);
        Map<Integer, IntArray> pi_sequence = new HashMap<>();
        Counter counter = new Counter();
        int pi = 2, p = 3;

        SegmentedSieve.Segment pi_sieve = sieve.segment(pi+1);
        SegmentedSieve.Segment p_sieve = sieve.segment(p+1);
        pi_sequence.put(2, new IntArray(new int[]{0, 1}));
        counter.add(1, 1);

        while (p <= limit) {
            IntArray current = pi_sequence.remove(pi);
            int next_pi = (int)pi_sieve.next();
            int next_p = (int)p_sieve.next();
            int gap = next_pi - pi;

            IntArray next = current.clone();
            next.a[0]++;
            if (p <= max_cache)
                pi_sequence.put(p, next);

            if (next_p < 0) {
                counter.add(next, 1, limit - p);
                break;
            } else {
                counter.add(next, 1, next_p - p);
            }

            p = next_p;
            if (gap > 1) {
                next = next.clone();
                next.add(0, 0);

                for (int i = 1; i < gap; i++) {
                    if (next_p <= max_cache)
                        pi_sequence.put(next_p, next);

                    next_p = (int)p_sieve.next();
                    if (next_p < 0) {
                        next_p = limit + 1;
                        gap = i + 1;
                        break;
                    }
                }

                counter.add(next, gap - 1, next_p - p);
            }

            pi = next_pi;
            p = next_p;
        }

        return counter.getResult(modulo);
    }

    public static void main(String[] args) {
        int n = 100_000_000;
        long m = 1_000_000_007;
        if (args.length > 0)
            n = Integer.parseInt(args[0]);
        if (args.length > 1)
            m = Long.parseLong(args[1]);
        System.out.println(solve(n, m));
    }
}
