package euler;

import euler.algo.DisjointSet;

public final class Problem186 {
    private Problem186() {}

    private static class Generator {
        private static final int SIZE = 55;
        private static final long A = 100003;
        private static final long B = 200003;
        private static final long C = 300007;

        private final int[] seed = new int[SIZE];
        private final int mod;
        private int index = 1;

        Generator(int mod) {
            this.mod = mod;
        }

        public int next() {
            int next;

            int k = index++;
            if (k <= 55) {
                next = (int)((A - B * k + C * k*k*k) % mod);
            } else {
                next = (get(k-24) + get(k-55)) % mod;
            }
            return set(k, next);
        }

        private int get(int i) {
            // i is 1-based index
            return seed[(i - 1) % SIZE];
        }

        private int set(int i, int x) {
            seed[(i - 1) % SIZE] = x;
            return x;
        }
    }

    public static int solve(int limit, int phone, int percentage) {
        Generator gen = new Generator(limit);
        DisjointSet record = new DisjointSet(limit);

        int calls = 0;
        while (record.componentSize(phone) < limit * percentage / 100) {
            int caller = gen.next();
            int called = gen.next();
            if (caller != called) {
                calls++;
                record.merge(caller, called);
            }
        }
        return calls;
    }

    public static void main(String[] args) {
        System.out.println(solve(1000000, 10086, 99));
    }
}
