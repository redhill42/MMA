package euler;

import euler.algo.Pythagorean;

public final class Problem75 {
    private Problem75() {}

    public static long solve(int limit) {
        int[] bent = new int[limit + 1];
        return Pythagorean.<Long>withHypotenuse(limit, 0L, (c, t) -> {
            int L = (int)t.perimeter();
            for (int k = L; k <= limit; k += L) {
                if (bent[k] == 0)
                    c++;
                else if (bent[k] == 1)
                    c--;
                bent[k]++;
            }
            return c;
        });
    }

    public static void main(String[] args) {
        int n = 1_500_000;
        if (args.length > 0)
            n = Integer.parseInt(args[0]);
        System.out.println(solve(n));
    }
}
