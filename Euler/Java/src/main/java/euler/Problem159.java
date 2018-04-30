package euler;

public final class Problem159 {
    private Problem159() {}

    public static int solve(int limit) {
        int[] mdrs = new int[limit + 1];
        for (int i = 2; i < limit; i++)
            mdrs[i] = digitalRoot(i);

        for (int a = 2; a <= limit / 2; a++)
            for (int b = 2; b <= a && a * b < limit; b++)
                mdrs[a * b] = Math.max(mdrs[a * b], mdrs[a] + mdrs[b]);

        int result = 0;
        for (int i = 2; i < limit; i++)
            result += mdrs[i];
        return result;
    }

    private static int digitalRoot(int n) {
        return 1 + (n - 1) % 9;
    }

    public static void main(String[] args) {
        int n = 1_000_000;
        if (args.length > 0)
            n = Integer.parseInt(args[0]);
        System.out.println(solve(n));
    }
}
