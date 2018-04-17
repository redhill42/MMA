package euler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static euler.util.Utils.isqrt;

public class Problem118 {
    static final int MAX = 98765432;

    BitSet primes = new BitSet(MAX);
    Map<Integer,Integer> candidates = new HashMap<>();

    @SuppressWarnings("unchecked")
    List<Integer>[] candidateGroups = new List[9];

    {
        for (int i = 1; i < candidateGroups.length; i++) {
            candidateGroups[i] = new ArrayList<>();
        }
    }

    int[][] partitions = {
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

    public Problem118() {
        primes.set(2);
        addCandidate(2);
        for (int i = 3; i < MAX; i += 2) {
            primes.set(i);
        }

        int sq = isqrt(MAX);
        for (int i = 3; i <= sq; i += 2) {
            if (primes.get(i)) {
                addCandidate(i);
                for (int j = i*i; j < MAX; j += i)
                    primes.clear(j);
            }
        }
        for (int i = ((sq+1)&~1)+1; i < MAX; i += 2) {
            if (primes.get(i))
                addCandidate(i);
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

    public static void main(String[] args) {
        System.out.println(new Problem118().solve());
    }
}