package euler;

import java.util.HashSet;
import java.util.Set;

import euler.algo.DisjointSet;

public final class Problem209 {
    private Problem209() {}

    public static long solve(final int n) {
        final int N = 1 << n;

        // precompute lucas numbers
        long[] lucas = new long[N];
        lucas[0] = 2; lucas[1] = 1;
        for (int i = 2; i < N; i++)
            lucas[i] = lucas[i-1] + lucas[i-2];

        // find cycles in input values
        DisjointSet set = new DisjointSet(N);
        for (int a = 0; a < N; a++) {
            int b = ((a << 1) & N-1) | (((a >> n-1) ^ ((a >> n-2) & (a >> n-3))) & 1);
            set.merge(a, b);
        }

        // compute combinatorics of truth table
        Set<Integer> seen = new HashSet<>();
        long result = 1;
        for (int i = 0; i < N; i++) {
            int root = set.find(i);
            if (!seen.contains(root)) {
                seen.add(root);
                result *= lucas[set.componentSize(root)];
            }
        }
        return result;
    }

    public static void main(String[] args) {
        int n = 6;
        if (args.length > 0)
            n = Integer.parseInt(args[0]);
        System.out.println(solve(n));
    }
}
