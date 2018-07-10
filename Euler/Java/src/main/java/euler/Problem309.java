package euler;

import euler.algo.Pythagorean;
import euler.util.IntArray;

public final class Problem309 {
    private Problem309() {}

    public static long solve(int limit) {
        IntArray[] triangles = new IntArray[limit];
        Pythagorean.withHypotenuse(limit, 0, (z, t) -> {
            int a = (int)t.a, b = (int)t.b, c = (int)t.c;
            while (c < limit) {
                if (triangles[a] == null)
                    triangles[a] = new IntArray();
                triangles[a].add(b);
                if (triangles[b] == null)
                    triangles[b] = new IntArray();
                triangles[b].add(a);
                a += t.a; b += t.b; c += t.c;
            }
            return 0;
        });

        long count = 0;
        for (IntArray candidates : triangles) {
            if (candidates == null || candidates.length == 1)
                continue;
            for (int i = 1; i < candidates.length; i++) {
                for (int j = 0; j < i; j++) {
                    long x = candidates.a[i], y = candidates.a[j];
                    if (x * y % (x + y) == 0)
                        count++;
                }
            }
        }
        return count;
    }

    public static void main(String[] args) {
        int n = 1_000_000;
        if (args.length > 0)
            n = Integer.parseInt(args[0]);
        System.out.println(solve(n));
    }
}
