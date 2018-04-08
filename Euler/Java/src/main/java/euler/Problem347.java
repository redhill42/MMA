package euler;

import java.util.HashSet;

public class Problem347 {
    public long solve(int N) {
        int[] sieve = new int[N + 1];
        int[][] pairs = new int[N + 1][2];

        for (int i = 2; i <= N; i++) {
            if (sieve[i] == 0) {
                for (int j = i; j <= N; j += i) {
                    if (sieve[j] < 2)
                        pairs[j][sieve[j]] = i;
                    sieve[j]++;
                }
            }
        }

        HashSet<Long> seen = new HashSet<>();
        long ans = 0;
        for (int n = N; n >= 2; n--) {
            if (sieve[n] == 2) {
                long pair = (long)pairs[n][0] * N + pairs[n][1];
                if (!seen.contains(pair)) {
                    seen.add(pair);
                    ans += n;
                }
            }
        }
        return ans;
    }

    public static void main(String[] args) {
        System.out.println(new Problem347().solve(10_000_000));
    }
}
