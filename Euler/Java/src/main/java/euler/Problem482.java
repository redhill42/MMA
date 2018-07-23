package euler;

import java.util.HashMap;
import java.util.Map;

import euler.algo.Pythagorean;
import euler.util.LongArray;

public final class Problem482 {
    private Problem482() {}

    public static long solve(int limit) {
        return prepare(limit).entrySet().parallelStream().mapToLong(e ->
            compute(e.getKey(), e.getValue(), limit)).sum();
    }

    private static Map<Integer, LongArray> prepare(int limit) {
        Map<Integer, LongArray> triangles = new HashMap<>();
        Pythagorean.withHypotenuse(limit / 2, 0L, (z, t) -> {
            long a = t.a, b = t.b, c = t.c;
            long L = a + b + c;
            for (int k = 1; k * L <= limit; k++) {
                triangles.computeIfAbsent((int)(a*k), x->new LongArray()).add((b*k << 32) | c*k);
                triangles.computeIfAbsent((int)(b*k), x->new LongArray()).add((a*k << 32) | c*k);
            }
            return 0L;
        });
        return triangles;
    }

    private static long compute(long r, LongArray candidates, int limit) {
        candidates.sort();

        long sum = 0;
        for (int i = 0; i < candidates.length; i++) {
            long x = (int)(candidates.a[i] >>> 32);
            long u = (int)(candidates.a[i]);

            for (int j = i; j < candidates.length; j++) {
                long y = (int)(candidates.a[j] >>> 32);
                long v = (int)(candidates.a[j]);

                long q = x * y - r * r;
                if (q <= 0 || r*u*v % q != 0)
                    continue;

                long w = r * u * v / q;
                long z = r * r * (x + y) / q;
                long p = 2 * (x + y + z);
                if (z >= y && p < limit)
                    sum += p + u + v + w;
            }
        }
        return sum;
    }

    public static void main(String[] args) {
        int n = 10_000_000;
        if (args.length > 0)
            n = Integer.parseInt(args[0]);
        System.out.println(solve(n));
    }
}
