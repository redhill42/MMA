package euler.util;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import static euler.util.Utils.exponent;
import static euler.util.Utils.isqrt;
import static euler.util.Utils.pow;

@SuppressWarnings("unused")
public class FactorizationSieve {
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
            return (int)pow(p, a);
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
    private final int nprimes;

    public FactorizationSieve(int limit) {
        int[] primes = new int[limit + 1];
        byte[] powers = new byte[limit + 1];
        int nprimes = 0;
        int plimit = isqrt(limit);
        int p;

        for (p = 2; p <= plimit; p++) {
            if (primes[p] == 0) {
                primes[p] = p;
                powers[p] = 1;
                nprimes++;
                for (int n = p * p; n <= limit; n += p) {
                    if (primes[n] == 0) {
                        primes[n] = p;
                        powers[n] = (byte)exponent(n, p);
                    }
                }
            }
        }
        for (; p <= limit; p++) {
            if (primes[p] == 0) {
                primes[p] = p;
                powers[p] = 1;
                nprimes++;
            }
        }

        this.primes = primes;
        this.powers = powers;
        this.nprimes = nprimes;
    }

    public boolean isPrime(int n) {
        return primes[n] == n;
    }

    public int nextPrime(int n) {
        if (n < 2)
            return 2;
        if (++n % 2 == 0)
            ++n;
        for (; n < primes.length; n += 2)
            if (primes[n] == n)
                return n;
        return -1;
    }

    public int previousPrime(int n) {
        if (n < 0)
            n = primes.length;
        if (n == 3)
            return 2;
        if (n <= 2)
            return -1;
        if (--n % 2 == 0)
            --n;
        for (; n >= 3; n -= 2)
            if (primes[n] == n)
                return n;
        return -1;
    }

    public int cardinality() {
        return nprimes;
    }

    public int[] getPrimes() {
        int[] result = new int[nprimes];
        int i = 0;

        result[i++] = 2;
        for (int n = 3; n < primes.length; n += 2)
            if (primes[n] == n)
                result[i++] = n;
        return result;
    }

    private int smallestFactor(int n) {
        return (int)pow(primes[n], powers[n]);
    }

    private int next(int n) {
        return n / smallestFactor(n);
    }

    public Set<Factor> factors(int n) {
        Set<Factor> result = new TreeSet<>();
        while (n != 1) {
            result.add(new Factor(primes[n], powers[n]));
            n = next(n);
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
            n = next(n);
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
            n = next(n);
        }
        return s;
    }

    public long sigma(int k, int n) {
        if (k == 0)
            return sigma(n);

        if (n < 0)
            n = -n;
        if (n <= 1)
            return n;

        long r = 1;
        while (n != 1) {
            int  p = primes[n];
            int  a = powers[n];
            long s = 1;
            for (int i = 1; i <= a; i++)
                s += pow(p, i * k);
            r *= s;
            n = next(n);
        }
        return r;
    }

    public int unitarySigma(int n) {
        if (n < 0)
            n = -n;
        if (n <= 1)
            return n;

        int r = 1;
        while (n != 1) {
            r <<= 1;
            n = next(n);
        }
        return r;
    }

    public long unitarySigma(int k, int n) {
        if (k == 0)
            return unitarySigma(n);

        if (n < 0)
            return -n;
        if (n <= 1)
            return n;

        long r = 1;
        while (n != 1) {
            int p = primes[n];
            int a = powers[n];
            r *= 1 + pow(p, k * a);
            n = next(n);
        }
        return r;
    }

    public int rad(int n) {
        if (n < 0)
            n = -n;
        if (n <= 1)
            return n;

        int r = 1;
        while (n != 1) {
            r *= primes[n];
            n = next(n);
        }
        return r;
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
            len++;
            n = next(n);
        }
        return len % 2 == 0 ? 1 : -1;
    }

    public int omega(int n) {
        if (n < 0)
            n = -n;
        if (n <= 1)
            return 0;

        int r = 0;
        while (n != 1) {
            r += powers[n];
            n = next(n);
        }
        return r;
    }

    public int nu(int n) {
        if (n < 0)
            n = -n;
        if (n <= 1)
            return 0;

        int r = 0;
        while (n != 1) {
            r++;
            n = next(n);
        }
        return r;
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
            n = next(n);
        }

        Arrays.sort(d);
        return d;
    }

    public int[] unitaryDivisors(int n) {
        if (n < 0)
            n = -n;

        int[] d = new int[unitarySigma(n)];
        int i = 0;

        if (n != 0)
            d[i++] = 1;
        if (n <= 1)
            return d;

        while (n != 1) {
            int k = i, u = smallestFactor(n);
            for (int j = 0; j < i; j++)
                d[k++] = d[j] * u;
            i = k;
            n /= u;
        }

        Arrays.sort(d);
        return d;
    }
}
