package euler;

import java.util.HashSet;

public final class Problem347 {
    private Problem347() {}

    public static long solve(int limit) {
        int[] sieve = new int[limit + 1];
        int[][] pairs = new int[limit + 1][2];

        for (int i = 2; i <= limit; i++) {
            if (sieve[i] == 0) {
                for (int j = i; j <= limit; j += i) {
                    if (sieve[j] < 2)
                        pairs[j][sieve[j]] = i;
                    sieve[j]++;
                }
            }
        }

        HashSet<Long> seen = new HashSet<>();
        long ans = 0;
        for (int n = limit; n >= 2; n--) {
            if (sieve[n] == 2) {
                long pair = (long)pairs[n][0] * limit + pairs[n][1];
                if (!seen.contains(pair)) {
                    seen.add(pair);
                    ans += n;
                }
            }
        }
        return ans;
    }

    public static void main(String[] args) {
        System.out.println(solve(10_000_000));
    }
}
