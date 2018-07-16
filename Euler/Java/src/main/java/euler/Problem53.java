package euler;

public final class Problem53 {
    private Problem53() {}

    public static int solve(int n, long limit) {
        long[] binomial = new long[n + 1];
        int count = 0;

        binomial[0] = 1;
        for (int k = 1; k <= n; k++) {
            for (int i = k; i >= 1; i--) {
                if (binomial[i] > limit || binomial[i-1] > limit)
                    binomial[i] = limit + 1; // keep big if overflow
                else
                    binomial[i] += binomial[i - 1];
                if (binomial[i] > limit)
                    count++;
            }
        }
        return count;
    }

    public static void main(String[] args) {
        int n = 100;
        if (args.length > 0)
            n = Integer.parseInt(args[0]);
        System.out.println(solve(n, 1_000_000));
    }
}
