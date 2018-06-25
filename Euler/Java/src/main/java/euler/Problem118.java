package euler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import euler.algo.PrimeSieve;
import euler.util.IntArray;

public final class Problem118 {
    private Problem118() {}

    private static class Solver {
        static final int MAX = 98765432;

        private final Map<Integer,Integer> candidates = new HashMap<>();

        @SuppressWarnings("unchecked")
        private final IntArray[] candidateGroups = new IntArray[9];
        {
            Arrays.setAll(candidateGroups, i -> new IntArray());
        }

        private final int[][] partitions = {
            {1, 8},
            {2, 7},
            {1, 1, 7},
            {3, 6},
            {1, 2, 6},
            {1, 1, 1, 6},
            {4, 5},
            {1, 3, 5},
            {2, 2, 5},
            {1, 1, 2, 5},
            {1, 1, 1, 1, 5},
            {1, 4, 4},
            {2, 3, 4},
            {1, 1, 3, 4},
            {1, 2, 2, 4},
            {1, 1, 1, 2, 4},
            {3, 3, 3},
            {1, 2, 3, 3},
            {1, 1, 1, 3, 3},
            {2, 2, 2, 3},
            {1, 1, 2, 2, 3},
            {1, 1, 1, 1, 2, 3},
            {1, 2, 2, 2, 2},
            {1, 1, 1, 2, 2, 2}
        };

        public Solver() {
            PrimeSieve sieve = new PrimeSieve(MAX);
            for (int p = 2; p > 0; p = sieve.nextPrime(p)) {
                addCandidate(p);
            }
        }

        private void addCandidate(int prime) {
            char[] digits = Integer.toString(prime).toCharArray();
            Arrays.sort(digits);
            if (!isPandigital(digits))
                return;
            prime = Integer.parseInt(new String(digits));

            Integer count = candidates.get(prime);
            if (count == null) {
                count = 1;
                candidateGroups[digits.length].add(prime);
            } else {
                count++;
            }
            candidates.put(prime, count);
        }

        public int solve() {
            int total = 0;
            int[] set = new int[10];
            for (int[] group : partitions) {
                total += countPrimeSets(group, 0, set);
            }
            return total;
        }

        private int countPrimeSets(int[] group, int i, int[] set) {
            int total = 0;

            if (i == group.length) {
                if (isPandigitalSet(set, i)) {
                    total = 1;
                    for (int j = 0; j < i; j++) {
                        total *= candidates.get(set[j]);
                    }
                }
            } else {
                for (int prime : candidateGroups[group[i]]) {
                    if (i == 0 || prime > set[i-1]) {
                        set[i] = prime;
                        total += countPrimeSets(group, i+1, set);
                    }
                }
            }

            return total;
        }

        private static boolean isPandigital(char[] digits) {
            char c = 0;
            for (char d : digits) {
                if (d == '0')
                    return false;
                if (d == c)
                    return false;
                c = d;
            }
            return true;
        }

        private static boolean isPandigitalSet(int[] set, int len) {
            StringBuilder buf = new StringBuilder();
            for (int i = 0; i < len; i++) {
                buf.append(set[i]);
            }

            char[] digits = buf.toString().toCharArray();
            Arrays.sort(digits);
            return isPandigital(digits);
        }
    }

    public static int solve() {
        return new Solver().solve();
    }

    public static void main(String[] args) {
        System.out.println(solve());
    }
}