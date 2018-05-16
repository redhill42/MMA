package euler;

import euler.algo.FactorizationSieve;

public final class Problem127 {
    private Problem127() {}

    static class Solver {
        private final int limit;
        private final FactorizationSieve sieve;

        public Solver(int limit) {
            this.limit = limit;
            this.sieve = new FactorizationSieve(limit);
        }

        public long solve() {
            return search1() + searchA(1, 2, 1);
        }

        private long search1() {
            long sum = 0;
            for (int b = 2; b < limit - 1; b++) {
                if ((long)sieve.rad(b) * sieve.rad(b + 1) < b + 1)
                    sum += b + 1;
            }
            return sum;
        }

        private long searchA(long seed, int p, long r) {
            long sum = 0;
            for (; p > 0 && seed * p < limit / 2; p = sieve.nextPrime(p)) {
                if (r * p >= limit)
                    break;
                for (long a = seed * p; a < limit / 2; a *= p) {
                    sum += searchB(a, 1, 2, r*p);
                    sum += searchA(a, sieve.nextPrime(p), r*p);
                }
            }
            return sum;
        }

        private long searchB(long a, long seed, int q, long r) {
            long sum = 0;
            for (; q > 0 && seed * q < limit - a; q = sieve.nextPrime(q)) {
                if (r % q == 0)
                    continue;
                long u = r * q;
                if (u >= limit)
                    break;
                for (long b = seed * q; b < limit - a; b *= q) {
                    if (a < b) {
                        // Fact: gcd(a,b)=1 implies gcd(a,c)=1 and gcd(b,c)=1
                        long c = a + b;
                        if (u * sieve.rad((int)c) < c) {
                            sum += c;
                        }
                    }
                    sum += searchB(a, b, sieve.nextPrime(q), u);
                }
            }
            return sum;
        }
    }

    public static long solve(int limit) {
        return new Solver(limit).solve();
    }

    public static void main(String[] args) {
        int limit = 120000;
        if (args.length > 0)
            limit = Integer.parseInt(args[0]);
        System.out.println(solve(limit));
    }
}
