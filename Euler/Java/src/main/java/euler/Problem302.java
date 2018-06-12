package euler;

import java.util.Arrays;
import euler.algo.FactorizationSieve;
import euler.algo.PrimeFactor;

import static euler.algo.Library.gcd;
import static euler.algo.Library.mul128;
import static euler.algo.Library.pow;
import static java.lang.Math.cbrt;

public final class Problem302 {
    private Problem302() {}

    private static class Solver {
        private final long limit;
        private final FactorizationSieve sieve;
        private final int[] primes;
        private final int[] powers;
        private final int[] phi_primes;
        private final int[] phi_powers;

        Solver(long limit) {
            int maxPrime = (int)cbrt(limit / 4);
            this.limit = limit;
            this.sieve = new FactorizationSieve(maxPrime);
            this.primes = new int[maxPrime + 1];
            this.powers = new int[maxPrime + 1];
            this.phi_primes = new int[maxPrime + 1];
            this.phi_powers = new int[maxPrime + 1];
        }

        public long solve() {
            long count = 0;
            for (int hp = 3; hp > 0; hp = sieve.nextPrime(hp)) {
                long n = (long)hp * hp * hp;
                for (int ha = 3; n < limit; n = mul128(n, hp), ha++) {
                    count += search(2, hp, ha, n, ha, 0);
                }
            }
            return count;
        }

        private long search(int p, int hp, int ha, long n, int gcd, int k) {
            long count = 0;

            for (; p > 0 && p < hp; p = sieve.nextPrime(p)) {
                long next = mul128(n, (long)p * p);
                if (next >= limit)
                    break;

                for (int a = 2; next < limit; next = mul128(next, p), a++) {
                    primes[k] = p;
                    powers[k] = a - 1;
                    primes[k + 1] = hp;
                    powers[k + 1] = ha - 1;

                    int g = gcd == 1 ? 1 : gcd(a, gcd);
                    if (g == 1 && isAchillesPhi(k + 2))
                        count++;
                    count += search(sieve.nextPrime(p), hp, ha, next, g, k+1);
                }
            }
            return count;
        }

        private boolean isAchillesPhi(int k) {
            int[] primes = this.primes;
            int[] powers = this.powers;
            int[] phi_primes = this.phi_primes;
            int[] phi_powers = this.phi_powers;

            System.arraycopy(primes, 0, phi_primes, 0, k);
            System.arraycopy(powers, 0, phi_powers, 0, k);

            int h = k;
            for (int i = 0; i < k; i++) {
                for (PrimeFactor f : sieve.factors(primes[i] - 1)) {
                    int j = Arrays.binarySearch(phi_primes, 0, h, (int)f.prime());
                    if (j < 0) {
                        j = -(j + 1);
                        if (j < h) {
                            System.arraycopy(phi_primes, j, phi_primes, j+1, h-j);
                            System.arraycopy(phi_powers, j, phi_powers, j+1, h-j);
                        }
                        phi_primes[j] = (int)f.prime();
                        phi_powers[j] = f.power();
                        h++;
                    } else {
                        phi_powers[j] += f.power();
                    }
                }
            }

            int gcd = phi_powers[0];
            for (int i = 0; i < h; i++) {
                if (phi_powers[i] < 2)
                    return false;
                gcd = gcd(gcd, phi_powers[i]);
            }
            return gcd == 1;
        }
    }

    public static long solve(long limit) {
        Solver solver = new Solver(limit);
        return solver.solve();
    }


    public static void main(String[] args) {
        int e = 18;
        if (args.length > 0)
            e = Integer.parseInt(args[0]);
        System.out.println(solve(pow(10L, e)));
    }
}
