package euler.util;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import static euler.util.Utils.exponent;
import static euler.util.Utils.isqrt;
import static euler.util.Utils.pow;

public class FactorizationSieve extends PrimeSieve {
    public static final class Factor implements Comparable<Factor> {
        private final int p, a;

        Factor(int p, int a) {
            this.p = p;
            this.a = a;
        }

        public int prime() {
            return p;
        }

        public int power() {
            return a;
        }

        public int value() {
            return pow(p, a);
        }

        @Override
        public int compareTo(Factor other) {
            return Integer.compare(p, other.p);
        }

        public boolean equals(Object obj) {
            if (obj instanceof Factor) {
                Factor other = (Factor)obj;
                return p == other.p && a == other.a;
            }
            return false;
        }

        public int hashCode() {
            return p * 31 + a;
        }

        public String toString() {
            return "{" + p + "," + a + "}";
        }
    }

    private final int[] primes;
    private final byte[] powers;

    public FactorizationSieve(int limit) {
        super(limit);

        primes = new int[limit + 1];
        powers = new byte[limit + 1];
        primes[1] = 1;
        powers[1] = 1;

        int plimit = isqrt(limit);
        for (int p = 2; p > 0; p = nextPrime(p)) {
            primes[p] = p;
            powers[p] = 1;
            if (p <= plimit) {
                for (int n = p * p; n <= limit; n += p) {
                    if (primes[n] == 0) {
                        primes[n] = p;
                        powers[n] = (byte)exponent(n, p);
                    }
                }
            }
        }
    }

    private int smallestFactor(int n) {
        return pow(primes[n], powers[n]);
    }

    public Set<Factor> factors(int n) {
        Set<Factor> result = new TreeSet<>();
        while (n != 1) {
            result.add(new Factor(primes[n], powers[n]));
            n /= smallestFactor(n);
        }
        return result;
    }

    public int phi(int n) {
        if (n < 0)
            n = -n;
        if (n <= 1)
            return n;

        int r = n;
        while (n != 1) {
            int p = primes[n];
            r = r / p * (p - 1);
            n /= smallestFactor(n);
        }
        return r;
    }

    public int sigma(int n) {
        if (n < 0)
            n = -n;
        if (n <= 1)
            return n;

        int s = 1;
        while (n != 1) {
            s *= powers[n] + 1;
            n /= smallestFactor(n);
        }
        return s;
    }

    public int sigma(int k, int n) {
        if (k == 0)
            return sigma(n);

        if (n < 0)
            n = -n;
        if (n <= 1)
            return n;

        int s = 1;
        while (n != 1) {
            int p = primes[n], a = powers[n];
            s *= (pow(p, k * (a + 1)) - 1) / (pow(p, k) - 1);
            n /= smallestFactor(n);
        }
        return s;
    }

    public int moebius(int n) {
        if (n < 0)
            n = -n;
        if (n <= 1)
            return n;

        int len = 0;
        while (n != 1) {
            if (powers[n] > 1)
                return 0;
            n /= smallestFactor(n);
            len++;
        }
        return len % 2 == 0 ? 1 : -1;
    }

    public int[] divisors(int n) {
        if (n < 0)
            n = -n;

        int[] d = new int[sigma(n)];
        int i = 0;

        if (n != 0)
            d[i++] = 1;
        if (n <= 1)
            return d;

        while (n != 1) {
            int k = i, p = primes[n], a = powers[n];
            while (--a >= 0) {
                for (int j = 0; j < i; j++)
                    d[k++] = d[j] * p;
                p *= primes[n];
            }
            i = k;
            n /= smallestFactor(n);
        }

        Arrays.sort(d);
        return d;
    }
}
